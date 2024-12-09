package com.example.peaceful_land.Service;

import com.example.peaceful_land.DTO.*;
import com.example.peaceful_land.Entity.*;
import com.example.peaceful_land.Exception.PostNotFoundException;
import com.example.peaceful_land.Exception.PropertyNotFoundException;
import com.example.peaceful_land.Query.PropertySpecification;
import com.example.peaceful_land.Repository.*;
import com.example.peaceful_land.Utils.ImageUtils;
import com.example.peaceful_land.Utils.VariableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static com.example.peaceful_land.Utils.VariableUtils.TYPE_UPLOAD_POST_THUMBNAIL;

@Service @RequiredArgsConstructor
public class PostService implements IPostService {

    private final AccountRepository accountRepository;
    private final RequestPostRepository requestPostRepository;
    private final PropertyLogRepository propertyLogRepository;
    private final PostRepository postRepository;
    private final PostLogRepository postLogRepository;
    private final UserInterestRepository userInterestRepository;
    private final PropertyRepository propertyRepository;
    private final IEmailService emailService;
    private final RequestTourRepository requestTourRepository;
    private final RequestContactRepository requestContactRepository;

    @Override
    public Post createPost(PostRequest request) {
        try {
            // Lấy ra bất động sản từ request
            Property property = propertyRepository.findById(request.getPropertyId()).orElse(null);
            if (property == null) throw new PropertyNotFoundException();
            // Ném ngoại lệ nếu bất động sản đã có bài rao
            if (postRepository.existsByProperty(property)) {
                throw new RuntimeException("Bất động sản đã có bài rao");
            }
            // Tạo post mới từ request và lưu vào database
            Post newPost = request.parsePostWithoutProperty();
            newPost.setProperty(property);
            newPost = postRepository.save(newPost);
            // Chuyển sang trạng thái ẩn (chờ duyệt)
            newPost.setHide(true);
            postRepository.save(newPost);
            // Lưu vào nhật ký thay đổi
            postLogRepository.save(newPost.toPostLog());
            // Trả về post mới
            return newPost;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Post checkPostExists(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new RuntimeException("Bài rao không tồn tại"));
    }

    @Override
    public String changeThumbnail(ChangePostThumbnailRequest request) {
        // Kiểm tra tài khoản tồn tại
        Post post = postRepository.findById(request.getPost_id()).orElse(null);
        if (post == null) throw new RuntimeException("Bài rao không tồn tại");
        // Kiểm tra file hợp lệ
        MultipartFile file = request.getImage();
        ImageUtils.checkImageFile(file);
        // Lấy tên file cũ
        String oldThumbnail = post.getThumbnUrl();
        // Tạo thư mục upload nếu chưa tồn tại
        try {
            ImageUtils.createUploadDirIfNotExists(TYPE_UPLOAD_POST_THUMBNAIL);
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi tạo thư mục upload: " + e.getMessage());
        }
        // Thực hiện thay đổi thumbnail
        try {
            // Lưu file vào server
            String fileName = ImageUtils.saveFileServer(file, TYPE_UPLOAD_POST_THUMBNAIL);
            // Cập nhật đường dẫn file mới vào database
            post.setThumbnUrl(fileName);
            postRepository.save(post);
            // Cập nhật vào bản ghi nhật ký mới nhất của bài rao
            PostLog postLog = postLogRepository.findTopByPostEqualsOrderByDateBeginDesc(post);
            postLog.setThumbnUrl(fileName);
            postLogRepository.save(postLog);
            // Xóa file cũ nếu không có postLog nào sử dụng
            if (!oldThumbnail.equals(VariableUtils.IMAGE_NA)) {
                if (!postLogRepository.existsByThumbnUrl(oldThumbnail)) {
                    ImageUtils.deleteFileServer(oldThumbnail);
                }
            }
            // Trả về thông báo thành công
            return "Đổi ảnh bìa của bài rao thành công. Đường dẫn mới: " + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi lưu tập tin: " + e.getMessage());
        }
    }

    @Override
    public RequestPost createUserPostRequestApproval(IdRequest postRequest) {
        // Kiểm tra nếu bài rao tồn tại
        Post post = postRepository.findById(postRequest.getPostId()).orElse(null);
        if (post == null) {
            throw new RuntimeException("Bài đăng không tồn tại");
        }
        // Lấy vai trò người dùng
        Byte role = post.getProperty().getUser().getRole();
        // Nếu role là môi giới VIP thì hiện bài đăng và bất động sản ngay (nhưng vẫn chờ duyệt)
        if (role.equals(VariableUtils.ROLE_BROKER_VIP)) {
            post.getProperty().setHide(false);
            propertyRepository.save(post.getProperty());
            post.setHide(false);
            postRepository.save(post);
        }
        // Lấy số ngày duyệt bài tối đa
        int noDayApprove = VariableUtils.getApprovalDayRange(role);
        // Tạo yêu cầu duyệt bài
        RequestPost requestPost = RequestPost.builder()
                .post(post)
                .expiration(LocalDate.now().plusDays(noDayApprove))
                .build();
        // Lưu yêu cầu
        return requestPostRepository.save(requestPost);
    }

    @Override
    public ViewPostResponse getPostInformationFromPostId(IdRequest request) {
        long userId = request.getUserId() == null ? -1 : request.getUserId();
        // Kiểm tra nếu người dùng tồn tại
        Optional<Account> account = accountRepository.findById(userId);
        // Kiểm tra nếu bài rao tồn tại
        Post post = postRepository.findById(request.getPostId()).orElse(null);
        if (post == null) {
            throw new RuntimeException("Bài rao không tồn tại");
        }
        // Kiểm tra nếu bài rao đã bị ẩn
        if (post.getHide()) {
            throw new RuntimeException("Bài rao đã bị ẩn");
        }
        // Lấy bài duyệt của bài rao này
        RequestPost requestPost = requestPostRepository.findByPostEquals(post);
        return createViewPostResponseFromInformation(account, post, requestPost);
    }

    public ViewPostResponse getPostInformationFromPost(IdRequest request) {
        long userId = request.getUserId() == null ? -1 : request.getUserId();
        // Kiểm tra nếu người dùng tồn tại
        Optional<Account> account = accountRepository.findById(userId);
        // Kiểm tra nếu bài rao đã bị ẩn
        Post post = request.getPost();
        if (post.getHide()) {
            throw new RuntimeException("Bài rao đã bị ẩn");
        }
        // Lấy bài duyệt của bài rao này
        RequestPost requestPost = requestPostRepository.findByPostEquals(post);
        return createViewPostResponseFromInformation(account, post, requestPost);
    }

    private ViewPostResponse getPostInformationFromProperty(IdRequest request) {
        Optional<Account> account = accountRepository.findById(request.getUserId());
        // Kiểm tra nếu bài rao tồn tại
        Post post = postRepository.findByProperty(request.getProperty());
        // Kiểm tra nếu bài rao đã bị ẩn
        if (post.getHide()) {
            throw new RuntimeException("Bài rao đã bị ẩn");
        }
        // Lấy bài duyệt của bài rao này
        RequestPost requestPost = requestPostRepository.findByPostEquals(post);
        return createViewPostResponseFromInformation(account, post, requestPost);
    }

    private ViewPostResponse createViewPostResponseFromInformation(Optional<Account> account, Post post, RequestPost requestPost) {
        ResponsePost responsePost = ResponsePost.fromPost(post);
        boolean isApproved = false;
        if (requestPost.getApproved() == null) {
            isApproved = true;
            responsePost.setTitle("Bài rao chờ duyệt #" + post.getId());
            responsePost.setDescription("Hãy nhấn vào nút quan tâm nếu bạn quan tâm đến bất động sản này. Nhớ bật thông báo, chúng tôi sẽ gửi thông báo về email của bạn khi bài rao được duyệt hoặc được cập nhật.");
            responsePost.getProperty().setLocation("Thông tin bị ẩn");
            responsePost.getProperty().setLocationDetail("Thông tin bị ẩn");
            responsePost.getProperty().setMapUrl("Thông tin bị ẩn");
            responsePost.getProperty().setUserId((long) -1);
        }
        // Trả kết quả nếu người dùng không tồn tại
        if (account.isEmpty()) {
            return ViewPostResponse.builder()
                    .data(responsePost)
                    .isPendingApproval(isApproved)
                    .build();
        }
        // Lấy thông tin quan tâm của người dùng
        Optional<UserInterest> userInterest = userInterestRepository
                .findByUserEqualsAndPropertyEquals(account.get(), post.getProperty());
        // Trả về thông tin phản hồi
        if (userInterest.isPresent()) {
            return ViewPostResponse.builder()
                    .data(responsePost)
                    .isPendingApproval(isApproved)
                    .interested(userInterest.get().getInterested())
                    .build();
        } else {
            return ViewPostResponse.builder()
                    .data(responsePost)
                    .isPendingApproval(isApproved)
                    .build();
        }
    }

    @Override
    public String interestPost(InterestPostRequest request) {
        Account account = accountRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new RuntimeException("Bài rao không tồn tại"));
        // Kiểm tra nếu bài rao đã bị ẩn
        if (post.getHide()) {
            throw new RuntimeException("Bài rao đã bị ẩn");
        }
        // Lưu thông tin quan tâm
        UserInterest userInterest = userInterestRepository.findByUserEqualsAndPropertyEquals(account, post.getProperty())
                .orElse(UserInterest.builder().build());
        // Cập nhật thông tin quan tâm, nếu như đã quan tâm (hoặc không quan tâm), mà nhấn lần nữa là xóa đi
        if (userInterest.getId() != null && userInterest.getInterested() == request.isInterested()) {
            userInterestRepository.delete(userInterest);
            return userInterest.getInterested() ? "Đã xóa dữ liệu quan tâm" : "Đã xóa dữ liệu không quan tâm";
        }
        // Cập nhật thông tin quan tâm mới (hoặc tạo mới nếu chưa có) và lưu vào database
        userInterest.setUser(account);
        userInterest.setProperty(post.getProperty());
        userInterest.setInterested(request.isInterested());
        userInterest.setNotification(request.isNotification());
        userInterestRepository.save(userInterest);
        return userInterest.getInterested() ? "Đã quan tâm bài đăng" : "Đã không quan tâm bài đăng";
    }

    @Override
    public void sendNotificationToInterestedUsers(Property property, String contentUpdate) {
        List<UserInterest> listUserInterested = userInterestRepository.findByPropertyEqualsAndNotificationEquals(property, true);
        if (!listUserInterested.isEmpty()) {
            new Thread(() -> {
                for (UserInterest userInterest : listUserInterested) {
                    emailService.sendPostUpdatedEmailToWhoInterested(
                            userInterest.getUser().getEmail(),
                            property.getId(),
                            userInterest.getDateBegin(),
                            contentUpdate
                    );
                }
            }).start();
        }
    }

    @Override
    public ResponsePostUpdatePermission getUpdatePostPermission(IdRequest request) {
        Post post = postRepository.findById(request.getPostId()).orElse(null);
        if (post == null) {
            throw new RuntimeException("Bài rao không tồn tại");
        }
        // Kiểm tra nếu bài rao đã bị ẩn
        if (post.getHide()) {
            throw new RuntimeException("Bài rao đã bị xóa");
        }
        // Nếu người dùng không phải là chủ sở hữu
        if (post.getProperty().getUser().getId() != request.getUserId()) {
            throw new RuntimeException("Bạn không có quyền cập nhật bài rao này");
        }
        // Nếu bài rao đã hết hạn
        if (post.getExpiration().isBefore(LocalDate.now())) {
            return ResponsePostUpdatePermission.builder()
                    .can_update(false)
                    .build();
        }
        List<String> listAction = new ArrayList<>();
        // Kiểm tra nếu bài rao còn sẵn sàng
        if (post.getProperty().getStatus()) {
            if (post.getProperty().getOffer()) {
                listAction.add(VariableUtils.UPDATE_TYPE_RENTED);
                listAction.add(VariableUtils.UPDATE_TYPE_RENTAL_PERIOD);
            } else {
                listAction.add(VariableUtils.UPDATE_TYPE_SOLD);
            }
            listAction.add(VariableUtils.UPDATE_TYPE_PRICE);
            listAction.add(VariableUtils.UPDATE_TYPE_OFFER);
            listAction.add(VariableUtils.UPDATE_TYPE_DISCOUNT);
            listAction.add(VariableUtils.UPDATE_TYPE_POST);
        } else {
            if (post.getProperty().getOffer()) {
                listAction.add(VariableUtils.UPDATE_TYPE_RERENT);
                listAction.add(VariableUtils.UPDATE_TYPE_RENTAL_PERIOD);
            } else {
                listAction.add(VariableUtils.UPDATE_TYPE_RESALE);
            }
        }
        return ResponsePostUpdatePermission.builder()
                .can_update(true)
                .actions(listAction)
                .build();
    }

    @Override
    public void updatePost_SoldOrRented(Post post, UpdatePropertyPostRequest request, boolean isSold) {
        // Cập nhật thông tin tình trạng bất động sản
        post.getProperty().setStatus(false);
        propertyRepository.save(post.getProperty());
        // Cập nhật vào bản ghi nhật ký mới nhất của bất động sản
        PropertyLog propertyLog = post.getProperty().toPropertyLog();
        propertyLog.setAction(request.getAction());
        propertyLogRepository.save(propertyLog);
        // Thông báo cho người quan tâm
        sendNotificationToInterestedUsers(
                post.getProperty(),
                "Bất động sản đã được " + (isSold ? "bán" : "cho thuê")
        );
    }

    @Override
    public void updatePost_ReSaleOrReRent(Post post, UpdatePropertyPostRequest request, boolean isReSale) {
        // Cập nhật thông tin tình trạng bất động sản
        post.getProperty().setStatus(true);
        if (request.getPrice() != null) {
            post.getProperty().setPrice(request.getPrice());
        }
        propertyRepository.save(post.getProperty());
        // Cập nhật vào bản ghi nhật ký mới nhất của bất động sản
        PropertyLog propertyLog = post.getProperty().toPropertyLog();
        propertyLog.setAction(request.getAction());
        propertyLogRepository.save(propertyLog);
        // Thông báo cho người quan tâm
        sendNotificationToInterestedUsers(
                post.getProperty(),
                "Bất động sản đã được " + (isReSale ? "mua bán lại" : "cho thuê lại") + " với giá " + propertyLog.getPrice()
        );
    }

    @Override
    public void updatePost_Price(Post post, UpdatePropertyPostRequest request) {
        // Cập nhật thông tin giá bất động sản
        Long oldPrice = post.getProperty().getPrice();
        if (Objects.equals(request.getPrice(), oldPrice)) {
            throw new RuntimeException("Giá mới không được trùng với giá cũ");
        }
        post.getProperty().setPrice(request.getPrice());
        propertyRepository.save(post.getProperty());
        // Cập nhật vào bản ghi nhật ký mới nhất của bất động sản
        PropertyLog propertyLog = post.getProperty().toPropertyLog();
        propertyLog.setAction(request.getAction());
        propertyLogRepository.save(propertyLog);
        // Thông báo cho người quan tâm
        sendNotificationToInterestedUsers(
                post.getProperty(),
                "Bất động sản đã được cập nhật giá từ " + oldPrice + " sang giá mới " + propertyLog.getPrice()
        );
    }

    @Override
    public void updatePost_Offer(Post post, UpdatePropertyPostRequest request) {
        // Cập nhật thông tin hình thức bất động sản
        post.getProperty().setOffer(!post.getProperty().getOffer());
        propertyRepository.save(post.getProperty());
        // Cập nhật vào bản ghi nhật ký mới nhất của bất động sản
        PropertyLog propertyLog = post.getProperty().toPropertyLog();
        propertyLog.setAction(request.getAction());
        propertyLogRepository.save(propertyLog);
        // Thông báo cho người quan tâm
        sendNotificationToInterestedUsers(
                post.getProperty(),
                "Bất động sản đã được " + (post.getProperty().getOffer() ? "đổi sang hình thức cho thuê" : "đổi sang hình thức bán")
        );
    }

    @Override
    public void updatePost_RentalPeriod(Post post, UpdatePropertyPostRequest request) {
        if (request.getRental_period().isBefore(LocalDate.now().plusMonths(6))) {
            throw new RuntimeException("Hạn cho thuê không được nhỏ hơn 6 tháng");
        }
        // Xử lý trường hợp thay đổi hạn cho thuê
        post.getProperty().setRentalPeriod(request.getRental_period());
        propertyRepository.save(post.getProperty());
        // Cập nhật vào bản ghi nhật ký mới nhất của bất động sản
        PropertyLog propertyLog = post.getProperty().toPropertyLog();
        propertyLog.setAction(request.getAction());
        propertyLogRepository.save(propertyLog);
        // Thông báo cho người quan tâm
        sendNotificationToInterestedUsers(
                post.getProperty(),
                "Bất động sản đã được cập nhật hạn cho thuê từ " + LocalDate.now() + " sang " + propertyLog.getRentalPeriod()
        );
    }

    @Override
    public void updatePost_Discount(Post post, UpdatePropertyPostRequest request) {
        throw new RuntimeException("Chức năng chưa được hỗ trợ");
//        if (request.getDiscount_expiration().isBefore(LocalDateTime.now())) {
//            throw new RuntimeException("Hạn giảm giá không được nhỏ hơn 6 giờ");
//        // Cập nhật thông tin giảm giá bất động sản
//        post.getProperty().setDiscount(request.getDiscount());
//        propertyRepository.save(post.getProperty());
//        // Cập nhật vào bản ghi nhật ký mới nhất của bất động sản
//        PropertyLog propertyLog = post.getProperty().toPropertyLog();
//        propertyLog.setAction(request.getAction());
//        propertyLogRepository.save(propertyLog);
//        // Thông báo cho ng
    }

    @Override
    public String updatePost_Information(Post post, UpdatePropertyPostRequest request) {
        // Cập nhật thông tin bất động sản
        post.setTitle(request.getTitle());
        post.setDescription(request.getDescription());
        postRepository.save(post);
        propertyRepository.save(post.getProperty());
        String contentUpdate = "Cập nhật thông tin thành công.";
        // Cập nhật ảnh bìa nếu có
        if (request.getThumbnail() != null) {
            try {
                changeThumbnail(ChangePostThumbnailRequest.builder()
                        .post_id(post.getId())
                        .image(request.getThumbnail())
                        .build());
                contentUpdate += " Cập nhật ảnh bìa mới thành công";
            } catch (Exception e) {
                contentUpdate += " Lỗi khi cập nhật ảnh bìa: " + e.getMessage();
            }
        }
        // Cập nhật vào bản ghi nhật ký mới nhất của bất động sản
        Post newPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("Bài rao không tồn tại"));
        postLogRepository.save(newPost.toPostLog());
        // Thông báo cho người quan tâm
        sendNotificationToInterestedUsers(post.getProperty(), "Bất động sản đã được cập nhật thông tin bài rao");
        return contentUpdate;
    }

    public ViewPostListResponse searchPost(SearchPostRequest request, int page, int size) {
        Specification<Property> spec = Specification.where(PropertySpecification.hasHideFalse());

        // Lọc các điều kiện khác
        Boolean offer = request.getOffer();
        Boolean status = request.getStatus();
        String location = request.getLocation();
        String category = request.getCategory();
        Long price = request.getPrice();
        List<ViewPostResponse> postIds = new LinkedList<>();
        Integer area = request.getArea();
        Integer bedrooms = request.getBedrooms();
        Integer toilets = request.getToilets();
        Byte entrance = request.getEntrance();
        Byte frontage = request.getFrontage();
        String houseOrientation = request.getHouseOrientation();
        String balconyOrientation = request.getBalconyOrientation();

        if (offer != null) {
            spec = spec.and(PropertySpecification.hasOffer(offer));
        }
        if (status != null) {
            spec = spec.and(PropertySpecification.hasStatus(status));
        }
        if (location != null) {
            spec = spec.and(PropertySpecification.hasLocation(location));
        }
        if (category != null) {
            spec = spec.and(PropertySpecification.hasCategory(category));
        }
        if (price != null) {
            spec = spec.and(PropertySpecification.hasPriceGreaterThan(price));
        }
        if (area != null) {
            spec = spec.and(PropertySpecification.hasAreaGreaterThan(area));
        }
        if (bedrooms != null) {
            spec = spec.and(PropertySpecification.hasBedroomsGreaterThan(bedrooms));
        }
        if (toilets != null) {
            spec = spec.and(PropertySpecification.hasToiletsGreaterThan(toilets));
        }
        if (entrance != null) {
            spec = spec.and(PropertySpecification.hasEntrance(entrance));
        }
        if (frontage != null) {
            spec = spec.and(PropertySpecification.hasFrontage(frontage));
        }
        if (houseOrientation != null) {
            spec = spec.and(PropertySpecification.hasHouseOrientation(houseOrientation));
        }
        if (balconyOrientation != null) {
            spec = spec.and(PropertySpecification.hasBalconyOrientation(balconyOrientation));
        }

        // Tìm kiếm và lấy các Property thỏa mãn
        Page<Property> propertiesPage = propertyRepository.findAll(spec, PageRequest.of(page, size, Sort.by("dateBegin").descending()));

        // Chuyển đổi Page<Property> thành Page<Long> (chỉ lấy id)
        propertiesPage.forEach(property -> {
            try {
                postIds.add(
                        getPostInformationFromProperty(IdRequest.builder()
                                .property(property)
                                .userId(request.getUserId()).build())
                );
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
        return ViewPostListResponse.builder().list_data(postIds).total_page(propertiesPage.getTotalPages()).build();
    }

    @Override
    public Object findNearestPosts(NearestPostsRequest request) {
        Page<Post> page = postRepository.findAllByHideEquals(
                false, PageRequest.of(0, request.getNumber(), Sort.by("dateBegin").descending())
        );
        return page.getContent().stream().toList().stream().map(post ->
                getPostInformationFromPost(IdRequest.builder().post(post).userId(request.getUserId()).build())
        ).toList();
    }

    @Override
    public Object getPropertyUpdateHistory(IdRequest request) {
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(PostNotFoundException::new);
        return propertyLogRepository.findAllByPropertyEqualsOrderByDateBeginDesc(post.getProperty())
                .stream().map(PropertyLog::toResponsePropertyLog).toList();
    }

    @Override
    public Object getPostUpdateHistory(IdRequest request) {
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(PostNotFoundException::new);
        return postLogRepository.findAllByPostEqualsOrderByDateBeginDesc(post)
                .stream().map(PostLog::toResponsePostLog).toList();
    }

    @Override
    public Object requestPermissionToContactAndTour(IdRequest request) {
        Post post = postRepository.findById(request.getPostId()).orElseThrow(PostNotFoundException::new);
        // Kiểm tra nếu bài rao đã bị ẩn
        if (post.getHide()) {
            throw new RuntimeException("Bài rao chưa được duyệt hoặc đã bị xóa");
        }
        // Kiểm tra nếu người dùng là chủ sở hữu
        if (post.getProperty().getUser().getId() == request.getUserId()) {
            throw new RuntimeException("Bạn không thể yêu cầu liên hệ trên bài rao của chính mình");
        }
        // Kiểm tra xem trong 2 ngày qua có yêu cầu không?
        if (requestContactRepository.existsByPropertyAndDateBeginBefore(post.getProperty(), LocalDateTime.now().plusDays(-2)) ||
            requestTourRepository.existsByPropertyAndDateBeginAfter(post.getProperty(), LocalDateTime.now().plusDays(-2))) {
            throw new RuntimeException("Bạn đã yêu cầu xem nhà hoặc liên hệ với bài rao này trong vòng 2 ngày qua");
        }
        return "Người dùng có quyền yêu cầu xem bất động sản hoặc yêu cầu liên hệ lại";
    }

    @Override
    public Object requestTour(Long postId, TourRequest request) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        // Kiểm tra nếu bài rao đã bị ẩn
        if (post.getHide()) {
            throw new RuntimeException("Bài rao chưa được duyệt hoặc đã bị xóa");
        }
        // Lấy thông tin bài rao
        RequestTour requestTour = RequestTour.fromTourRequestWithoutProperty(request);
        requestTour.setProperty(post.getProperty());
        // Lưu yêu cầu
        requestTourRepository.save(requestTour);
        return "Tạo yêu cầu tham quan bất động sản thành công.";
    }

    @Override
    public Object requestContact(Long postId, ContactRequest request) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        // Kiểm tra nếu bài rao đã bị ẩn
        if (post.getHide()) {
            throw new RuntimeException("Bài rao chưa được duyệt hoặc đã bị xóa");
        }
        // Lấy thông tin bài rao
        RequestContact requestContact = RequestContact.fromContactRequestWithoutProperty(request);
        requestContact.setProperty(post.getProperty());
        // Lưu yêu cầu
        requestContactRepository.save(requestContact);
        return "Tạo yêu cầu liên hệ lại thành công";
    }
}
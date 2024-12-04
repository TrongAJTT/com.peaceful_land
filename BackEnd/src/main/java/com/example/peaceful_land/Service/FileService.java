package com.example.peaceful_land.Service;

import com.example.peaceful_land.Entity.File;
import com.example.peaceful_land.Repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service @RequiredArgsConstructor
public class FileService implements IFileService {

    private final FileRepository fileRepository;

    @Override
    public File createFile(String fileUrl) {
        File file = File.builder().fileUrl(fileUrl).build();
        return fileRepository.save(file);
    }
}

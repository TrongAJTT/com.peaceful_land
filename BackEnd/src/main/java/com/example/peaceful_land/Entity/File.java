package com.example.peaceful_land.Entity;

import com.example.peaceful_land.DTO.FileDTO;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "file")
public class File {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column private long id;
    @Column private long ownerId;
    @Column private String name;
    @Column private String type;
    @Column private long size;
    @Column private String path;
    @Column private String meta;
    @Column private boolean hide;
    @Column private boolean order;
    @Column private String dateBegin;

    public File() {}

    public File(long id, long ownerId, String name, String type, long size, String path, String meta, boolean hide, boolean order, String dateBegin) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.type = type;
        this.size = size;
        this.path = path;
        this.meta = meta;
        this.hide = hide;
        this.order = order;
        this.dateBegin = dateBegin;
    }

    public File(long ownerId, String name, String type, long size, String path, String meta, boolean hide, boolean order, String dateBegin) {
        this.ownerId = ownerId;
        this.name = name;
        this.type = type;
        this.size = size;
        this.path = path;
        this.meta = meta;
        this.hide = hide;
        this.order = order;
        this.dateBegin = dateBegin;
    }

    public void setValues(FileDTO file) {
        this.id = file.getId();
        this.ownerId = file.getOwnerId();
        this.name = file.getName();
        this.type = file.getType();
        this.size = file.getSize();
        this.path = file.getPath();
        this.meta = file.getMeta();
        this.hide = file.isHide();
        this.order = file.isOrder();
        this.dateBegin = file.getDateBegin();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public boolean isHide() {
        return hide;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    public boolean isOrder() {
        return order;
    }

    public void setOrder(boolean order) {
        this.order = order;
    }

    public String getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(String dateBegin) {
        this.dateBegin = dateBegin;
    }
}

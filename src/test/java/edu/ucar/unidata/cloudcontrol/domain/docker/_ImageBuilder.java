package edu.ucar.unidata.cloudcontrol.domain.docker;

public class _ImageBuilder {

    private _Image image;

    public _ImageBuilder() {
        image = new _Image();
    }

    public _ImageBuilder id(String id) {
        image.setId(id);
        return this;
    }

    public _ImageBuilder isVisibleToUsers(boolean isVisibleToUsers) {
        image.setIsVisibleToUsers(isVisibleToUsers);
        return this;
    }

    public _Image build() {
        return image;
    }
}

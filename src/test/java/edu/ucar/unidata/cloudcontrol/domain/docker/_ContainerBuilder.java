package edu.ucar.unidata.cloudcontrol.domain.docker;

public class _ContainerBuilder {

    private _Container container;

    public _ContainerBuilder() {
        container = new _Container();
    }

    public _ContainerBuilder id(String id) {
        container.setId(id);
        return this;
    }

    public _Container build() {
        return container;
    }
}

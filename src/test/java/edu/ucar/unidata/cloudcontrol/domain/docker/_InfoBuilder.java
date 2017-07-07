package edu.ucar.unidata.cloudcontrol.domain.docker;

public class _InfoBuilder {

    private _Info info;

    public _InfoBuilder() {
        info = new _Info();
    }

    public _InfoBuilder architecture(String architecture) {
        info.setArchitecture(architecture);
        return this;
    }

    public _Info build() {
        return info;
    }
}

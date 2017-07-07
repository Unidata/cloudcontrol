package edu.ucar.unidata.cloudcontrol.domain.docker;

public class _VersionBuilder {

    private _Version version;

    public _VersionBuilder() {
        version = new _Version();
    }

    public _VersionBuilder apiVersion(String apiVersion) {
        version.setApiVersion(apiVersion);
        return this;
    }

    public _Version build() {
        return version;
    }
}

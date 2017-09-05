package edu.ucar.unidata.cloudcontrol.service.docker.converters;

import edu.ucar.unidata.cloudcontrol.domain.docker._Image;
import edu.ucar.unidata.cloudcontrol.domain.docker._InspectImageResponse;
import edu.ucar.unidata.cloudcontrol.domain.docker._GraphData;
import edu.ucar.unidata.cloudcontrol.domain.docker._GraphDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import javax.annotation.Resource;

import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.command.GraphData;
import com.github.dockerjava.api.command.GraphDriver;
import com.github.dockerjava.api.model.Image;

/**
 * Service for converting com.github.dockerjava.* Image-related objects to a corresponding edu.ucar.unidata.* objects.
 */
public class ImageConverter {

    /**
     * Utility method to process a List of com.github.dockerjava.api.model.Image objects
     * to a corresponding List of edu.ucar.unidata.cloudcontrol.domain.docker._Image objects.
     *
     * @param images  The com.github.dockerjava.api.model.Image List.
     * @return  A edu.ucar.unidata.cloudcontrol.domain.docker._Image List.
     */
    public List<_Image> processImageList(List<Image> images) {
        List<_Image> _images = new ArrayList<_Image>(images.size());
        for (Image image : images) {
            _Image _image = convertImage(image);
            if (Objects.nonNull(_image)) {
                _images.add(_image);
            }
        }
        if (_images.isEmpty()) {
            _images = null;
        }
        return _images;
    }

    /**
     * Converts a com.github.dockerjava.api.model.Image object to
     * a edu.ucar.unidata.cloudcontrol.domain.docker._Image object.
     *
     * @param image  The com.github.dockerjava.api.model.Image object to convert.
     * @return  A edu.ucar.unidata.cloudcontrol.domain.docker._Image object.
     */
    public _Image convertImage(Image image) {
        Function<Image, _Image> mapImageTo_Image = new Function<Image, _Image>() {
            public _Image apply(Image i) {
                _Image _image = new _Image();
                _image.setCreated(i.getCreated());
                _image.setId(i.getId());
                _image.setParentId(i.getParentId());
                _image.setRepoTags(i.getRepoTags());
                _image.setSize(i.getSize());
                _image.setVirtualSize(i.getVirtualSize());
                return _image;
            }
        };
        _Image _image = mapImageTo_Image.apply(image);
        return _image;
    }


    /**
     * Converts a com.github.dockerjava.api.command.InspectImageResponse object to
     * a edu.ucar.unidata.cloudcontrol.domain.docker._InspectImageResponse object.
     *
     * @param inspectImageResponse  The com.github.dockerjava.api.command.InspectImageResponse object to convert.
     * @return  A edu.ucar.unidata.cloudcontrol.domain.docker._InspectImageResponse object.
     */
    public _InspectImageResponse convertInspectImageResponse(InspectImageResponse inspectImageResponse) {
        Function<InspectImageResponse, _InspectImageResponse> mapInspectImageResponseTo_InspectImageResponse = new Function<InspectImageResponse, _InspectImageResponse>() {
            public _InspectImageResponse apply(InspectImageResponse i) {
              _InspectImageResponse _inspectImageResponse = new _InspectImageResponse();
              _inspectImageResponse.setArch(i.getArch());
              _inspectImageResponse.setAuthor(i.getAuthor());
              _inspectImageResponse.setComment(i.getComment());
              //_inspectImageResponse.setConfig(i.getConfig());
              _inspectImageResponse.setContainer(i.getContainer());
              //_inspectImageResponse.setContainerConfig(i.getContainerConfig());
              _inspectImageResponse.setCreated(i.getCreated());
              _inspectImageResponse.setDockerVersion(i.getDockerVersion());
              _inspectImageResponse.setId(i.getId());
              _inspectImageResponse.setOs(i.getOs());
              _inspectImageResponse.setParent(i.getParent());
              _inspectImageResponse.setSize(i.getSize());
              _inspectImageResponse.setRepoTags(i.getRepoTags());
              _inspectImageResponse.setRepoDigests(i.getRepoDigests());
              _inspectImageResponse.setVirtualSize(i.getVirtualSize());
             // _inspectImageResponse.setGraphDriver(convertGraphDriver(i.getGraphDriver()));
              return _inspectImageResponse;
            }
        };
        _InspectImageResponse _inspectImageResponse = mapInspectImageResponseTo_InspectImageResponse.apply(inspectImageResponse);
        return _inspectImageResponse;
    }

    /**
     * Converts a com.github.dockerjava.api.command.GraphDriver object to
     * a edu.ucar.unidata.cloudcontrol.domain.docker._GraphDriver object.
     *
     * @param graphDriver  The com.github.dockerjava.api.command.GraphDriver object to convert.
     * @return  A edu.ucar.unidata.cloudcontrol.domain.docker._GraphDriver object.
     */
    public _GraphDriver convertGraphDriver(GraphDriver graphDriver) {
        Function<GraphDriver, _GraphDriver> mapGraphDriverTo_GraphDriver = new Function<GraphDriver, _GraphDriver>() {
            public _GraphDriver apply(GraphDriver g) {
                _GraphDriver _graphDriver = new _GraphDriver();
                _graphDriver.setName(g.getName());
                _graphDriver.setData(convertGraphData(g.getData()));
                return _graphDriver;
            }
        };
        _GraphDriver _graphDriver = mapGraphDriverTo_GraphDriver.apply(graphDriver);
        return _graphDriver;
    }

    /**
     * Converts a com.github.dockerjava.api.command.GraphData object to
     * a edu.ucar.unidata.cloudcontrol.domain.docker._GraphData object.
     *
     * @param graphData  The com.github.dockerjava.api.command.GraphData object to convert.
     * @return  A edu.ucar.unidata.cloudcontrol.domain.docker._GraphData object.
     */
    public _GraphData convertGraphData(GraphData graphData) {
        Function<GraphData, _GraphData> mapGraphDataTo_GraphData = new Function<GraphData, _GraphData>() {
            public _GraphData apply(GraphData g) {
                _GraphData _graphData = new _GraphData();
                _graphData.setRootDir(g.getRootDir());
                _graphData.setDeviceId(g.getDeviceId());
                _graphData.setDeviceName(g.getDeviceName());
                _graphData.setDeviceSize(g.getDeviceSize());
                return _graphData;
            }
        };
        _GraphData _graphData = mapGraphDataTo_GraphData.apply(graphData);
        return _graphData;
    }
}

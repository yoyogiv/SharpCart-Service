package com.sharpcart.rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import com.sharpcart.rest.model.SharpList;
import com.sharpcart.rest.model.StorePrices;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/aggregators/optimize")
public class OptimizeSharpListController {

    private static Logger LOG = LoggerFactory.getLogger(OptimizeSharpListController.class);

    @RequestMapping(method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String optimizeSharpList(@RequestBody final SharpList sharpList) {
    	
    	//testing to see if we get a valid json object from the android device
    	LOG.info("User Name: "+sharpList.getUserName());
    	
    	return "we got the json";
    }

}

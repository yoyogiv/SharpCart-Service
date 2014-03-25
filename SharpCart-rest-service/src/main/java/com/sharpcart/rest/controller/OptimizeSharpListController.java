package com.sharpcart.rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sharpcart.rest.model.SharpList;

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

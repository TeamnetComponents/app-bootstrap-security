package ro.teamnet.bootstrap.web.rest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ro.teamnet.bootstrap.domain.ModuleRight;
import ro.teamnet.bootstrap.extend.AppPageable;
import ro.teamnet.bootstrap.service.ModuleRightService;

import javax.inject.Inject;
import java.util.List;

/**
 * REST controller for managing moduleright.
 */
@RestController
@RequestMapping("/app/rest/modulerights")
public class ModuleRightResource extends ro.teamnet.bootstrap.web.rest.AbstractResource<ModuleRight, Long> {

    private final Logger log = LoggerFactory.getLogger(ModuleRightResource.class);


    ModuleRightService moduleRightService;

    @Inject
    public ModuleRightResource(ModuleRightService moduleRightService) {
        super(moduleRightService);
        this.moduleRightService = moduleRightService;
    }

    @RequestMapping(value = "/codes",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List> getAllCodes(AppPageable appPageable) {
        log.debug("REST request to get all moduleright");
        return new ResponseEntity<>(moduleRightService.getModuleRightCodes(), HttpStatus.OK);
    }

}
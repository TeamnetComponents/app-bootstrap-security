package ro.teamnet.bootstrap.web.rest;


import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.teamnet.bootstrap.domain.ModuleRight;
import ro.teamnet.bootstrap.extend.AppPage;
import ro.teamnet.bootstrap.extend.AppPageable;
import ro.teamnet.bootstrap.service.ModuleRightService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * REST controller for managing moduleright.
 */
@RestController
@RequestMapping("/app/rest")
public class ModuleRightResource extends ro.teamnet.bootstrap.web.rest.AbstractResource<ModuleRight, Long> {

    private final Logger log = LoggerFactory.getLogger(ModuleRightResource.class);


    ModuleRightService moduleRightService;

    @Inject
    public ModuleRightResource(ModuleRightService moduleRightService) {
        super(moduleRightService);
        this.moduleRightService = moduleRightService;
    }

    /**
     * POST  /rest/moduleright/:id -> create a new moduleright.
     */
    @RequestMapping(value = "/modulerights",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void create(@RequestBody ModuleRight moduleRight) {
        super.create(moduleRight);
    }

    /**
     * GET  /rest/moduleright/:id -> get all the moduleright.
     */
    @RequestMapping(value = "/modulerights",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AppPage<ModuleRight> getAll(AppPageable appPageable) {
       return super.getAll(appPageable);
    }

    /**
     * GET  /rest/moduleright -> get the "id" moduleright.
     */
    @RequestMapping(value = "/modulerights/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ModuleRight> get(@PathVariable Long id,HttpServletResponse response) {
        return super.get(id, response);
    }

    /**
     * DELETE   /rest/moduleright/:id -> delete the "id" moduleright
     */
    @RequestMapping(value = "/modulerights/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable Long id) {
        super.delete(id);
    }

    @RequestMapping(value = "/modulerightscodes",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List> getAllCodes(AppPageable appPageable) {
        log.debug("REST request to get all moduleright");
        return new ResponseEntity<>(moduleRightService.getModuleRightCodes(), HttpStatus.OK);
    }

}
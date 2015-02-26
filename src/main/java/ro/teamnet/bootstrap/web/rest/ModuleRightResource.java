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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * REST controller for managing moduleright.
 */
@RestController
@RequestMapping("/moduleright")
public class ModuleRightResource {

    private final Logger log = LoggerFactory.getLogger(ModuleRightResource.class);

    @Inject
    ModuleRightService moduleRightService;

    /**
     * POST  /rest/moduleright/:id -> create a new moduleright.
     */
    @RequestMapping(value = "/rest/modulerights",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> create(@RequestBody ModuleRight moduleRight, HttpServletRequest request,
                       HttpServletResponse response) {
        log.debug("REST request to save moduleright : {}", moduleRight);
        if(moduleRightService.getOne(moduleRight.getId()) != null)
        {
            return new ResponseEntity<String>("ModuleRight allready exists", HttpStatus.BAD_REQUEST);
        }
        moduleRightService.save(moduleRight);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * GET  /rest/moduleright/:id -> get all the moduleright.
     */
    @RequestMapping(value = "/rest/modulerights",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AppPage<ModuleRight> getAll(AppPageable appPageable) {
        log.debug("REST request to get all moduleright");
        return  moduleRightService.findAll(appPageable);
    }

    /**
     * GET  /rest/moduleright -> get the "id" moduleright.
     */
    @RequestMapping(value = "/rest/modulerights/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ModuleRight> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get the moduleright : {}", id);
        ModuleRight moduleRight = moduleRightService.getOne(id);
        if (moduleRight == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(moduleRight, HttpStatus.OK);
    }

    /**
     * DELETE   /rest/moduleright/:id -> delete the "id" moduleright
     */
    @RequestMapping(value = "/rest/modulerights/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete ModuleRight : {}", id);
        moduleRightService.delete(id);
    }
}
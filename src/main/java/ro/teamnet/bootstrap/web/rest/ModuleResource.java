package ro.teamnet.bootstrap.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.teamnet.bootstrap.domain.Module;
import ro.teamnet.bootstrap.extend.AppPage;
import ro.teamnet.bootstrap.extend.AppPageable;
import ro.teamnet.bootstrap.service.ModuleService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * REST controller for managing modules.
 */
@RestController
@RequestMapping(value = "/module")
public class ModuleResource {

    private final Logger log = LoggerFactory.getLogger(ModuleRightResource.class);

    @Inject
    ModuleService moduleService;

    /**
     * POST  /rest/modules/:id -> create a new module.
     */
    @RequestMapping(value = "/rest/modules",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> create(@RequestBody Module module, HttpServletRequest request,
                                    HttpServletResponse response) {
        log.debug("REST request to save Module : {}", module);
        if(moduleService.getOne(module.getId()) != null)
        {
            return new ResponseEntity<String>("Module allready exists", HttpStatus.BAD_REQUEST);
        }
        moduleService.save(module);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * GET  /rest/modules -> get the all modules.
     */
    @RequestMapping(value = "/rest/modules",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AppPage<Module> getAll(AppPageable appPageable) {
        log.debug("REST request to get all modules");
        return  moduleService.findAll(appPageable);
    }

    /**
     * GET  /rest/modules/:id -> get the "ïd" module
     */
    @RequestMapping(value = "/rest/modules/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Module> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get the module : {}", id);
        Module module = moduleService.getOne(id);
        if (module == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(module, HttpStatus.OK);
    }

    /**
     * DELETE   /rest/modules/:id -> delete the "id" module
     */
    @RequestMapping(value = "/rest/modules/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Module : {}", id);
        moduleService.delete(id);
    }

}

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
import ro.teamnet.bootstrap.web.rest.dto.ModuleDTO;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
        moduleService.save(module);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * POST  /rest/module -> update module
     */
    @RequestMapping(value = "/rest/role/{id}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> update(@PathVariable Long id,@RequestBody ModuleDTO moduleDTO) {
        log.debug("REST request to update the module : {}", id);
        Module module = moduleService.getOne(id);
        if(module == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        moduleService.update(module, moduleDTO);
        return new ResponseEntity<>(HttpStatus.OK);
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

    @RequestMapping(value = "/rest/modulesWithModuleRights",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Module> getAllModulesWithModuleRights() {
        log.debug("REST request to get all modules");
        return  moduleService.getAllModulesWithModuleRights();
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

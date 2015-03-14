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
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * REST controller for managing modules.
 */
@RestController
@RequestMapping(value = "/app/rest")
public class ModuleResource extends ro.teamnet.bootstrap.web.rest.AbstractResource<Module,Long>{

    private final Logger log = LoggerFactory.getLogger(ModuleRightResource.class);


    private ModuleService moduleService;

    @Inject
    public ModuleResource(ModuleService moduleService) {
        super(moduleService);
        this.moduleService=moduleService;
    }

    /**
     * POST  /rest/modules/:id -> create a new module.
     */
    @RequestMapping(value = "/module",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void create(@RequestBody Module module) {
        super.create(module);
    }

    /**
     * POST  /rest/module -> update module
     */
    @RequestMapping(value = "/role/{id}",
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
    @RequestMapping(value = "/module",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AppPage<Module> getAll(AppPageable appPageable) {
        return super.getAll(appPageable);
    }

    @RequestMapping(value = "/module/rights",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Module> getAllModulesWithModuleRights() {
        log.debug("REST request to get all modules");
        return  moduleService.getAllModulesWithModuleRights();
    }

    /**
     * GET  /rest/modules/:id -> get the "ïd" module
     */
    @RequestMapping(value = "/module/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Module> get(@PathVariable Long id, HttpServletResponse response) {
        return super.get(id, response);
    }

    /**
     * DELETE   /rest/modules/:id -> delete the "id" module
     */
    @RequestMapping(value = "/module/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable Long id) {
        super.delete(id);
    }

}

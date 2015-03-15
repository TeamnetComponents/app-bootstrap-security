package ro.teamnet.bootstrap.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.teamnet.bootstrap.domain.Module;
import ro.teamnet.bootstrap.service.ModuleService;
import ro.teamnet.bootstrap.web.rest.dto.ModuleDTO;

import javax.inject.Inject;
import java.util.List;

/**
 * REST controller for managing modules.
 */
@RestController
@RequestMapping(value = "/app/rest/module")
public class ModuleResource extends ro.teamnet.bootstrap.web.rest.AbstractResource<Module,Long>{

    private final Logger log = LoggerFactory.getLogger(ModuleRightResource.class);


    private ModuleService moduleService;

    @Inject
    public ModuleResource(ModuleService moduleService) {
        super(moduleService);
        this.moduleService=moduleService;
    }

    /**
     * POST  /rest/module -> update module
     */
    @RequestMapping(value = "/{id}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> update(@PathVariable Long id,@RequestBody ModuleDTO moduleDTO) {
        log.debug("REST request to update the module : {}", id);

        moduleService.update(id, moduleDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/rights",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Module> getAllModulesWithModuleRights() {
        log.debug("REST request to get all modules");
        return  moduleService.getAllModulesWithModuleRights();
    }



}

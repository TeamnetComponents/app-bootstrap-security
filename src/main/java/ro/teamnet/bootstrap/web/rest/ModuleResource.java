package ro.teamnet.bootstrap.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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
import java.util.Set;

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
    public ResponseEntity<?> updateById(@PathVariable Long id,@RequestBody ModuleDTO moduleDTO) throws JsonProcessingException{
        log.debug("REST request to update the module : {}", id);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        if(!moduleService.update(id, moduleDTO)){
            return new ResponseEntity<>(ow.writeValueAsString(HttpStatus.FORBIDDEN),HttpStatus.OK);
        }
        return new ResponseEntity<>(ow.writeValueAsString(HttpStatus.OK),HttpStatus.OK);
    }

    @RequestMapping(value = "/rights",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<Module> getAllModulesWithModuleRights() {
        log.debug("REST request to get all modules");
        return  moduleService.getAllModulesWithModuleRights();
    }

    /**
     * GET  /rest/modules/:id -> get the "ïd" module
     */
    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Override
    public ResponseEntity<Module> get(@PathVariable Long id) {
        log.debug("REST request to get the module : {}", id);
        Module module = moduleService.getOne(id);
        if (module == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(module, HttpStatus.OK);
    }

}

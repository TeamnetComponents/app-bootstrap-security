package ro.teamnet.bootstrap.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.teamnet.bootstrap.domain.Role;
import ro.teamnet.bootstrap.service.RoleService;
import ro.teamnet.bootstrap.web.rest.dto.RoleDTO;

import javax.inject.Inject;
import java.util.Set;

/**
 * REST controller for managing role.
 */
@RestController
@RequestMapping("/app/rest/role")
public class RoleResource extends ro.teamnet.bootstrap.web.rest.AbstractResource<Role,Long>{

    private final Logger log = LoggerFactory.getLogger(RoleResource.class);
    private RoleService roleService;

    @Inject
    public RoleResource(RoleService roleService) {
        super(roleService);
        this.roleService=roleService;
    }

    @RequestMapping(value = "/allWithModuleRights", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Set<Role> getAllWithModuleRights() {
        log.debug("REST request to get all roles fetching module rights olso");
        return roleService.getAllWithModuleRights();
    }
    
    /**
     * POST  /rest/role -> update role
     */
    @RequestMapping(value = "/{id}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Role> updateById(@PathVariable Long id,@RequestBody RoleDTO roleDTO) {
        log.debug("REST request to update the role : {}", id);
        Boolean roleFound = roleService.updateRoleById(id, roleDTO);
        if (roleFound == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

//        Role role = roleService.getOneById(id);
//        if(role == null){
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        roleService.update(role, roleDTO);

        return new ResponseEntity<Role>(role, HttpStatus.OK);
    }


    @Override
    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Role> get(@PathVariable Long id) {
        log.debug("REST request to get  : {}", id);
        Role role = roleService.getOneById(id);
        if (role == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(role, HttpStatus.OK);
    }
}

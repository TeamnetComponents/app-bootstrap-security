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

    /**
     * POST  /rest/role -> update role
     */
    @RequestMapping(method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> update(@RequestBody Role role) {
        log.debug("REST request to update the role : {}", role.getId());
        return new ResponseEntity<>(roleService.update(role), HttpStatus.OK);
    }

    /**
     * POST  /rest/role -> update role
     */
    @RequestMapping(value = "/role/{id}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> update(@PathVariable Long id,@RequestBody RoleDTO roleDTO) {
        log.debug("REST request to update the role : {}", id);
        Role role = roleService.getOne(id);
        if(role == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        roleService.update(role, roleDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}

package ro.teamnet.bootstrap.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.teamnet.bootstrap.domain.Role;
import ro.teamnet.bootstrap.extend.AppPage;
import ro.teamnet.bootstrap.extend.AppPageable;
import ro.teamnet.bootstrap.service.RoleService;
import ro.teamnet.bootstrap.web.rest.dto.RoleDTO;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

/**
 * REST controller for managing role.
 */
@RestController
@RequestMapping("/app/rest")
public class RoleResource extends ro.teamnet.bootstrap.web.rest.AbstractResource<Role,Long>{

    private final Logger log = LoggerFactory.getLogger(RoleResource.class);


    private RoleService roleService;

    @Inject
    public RoleResource(RoleService roleService) {
        super(roleService);
        this.roleService=roleService;
    }

    /**
     * POST  /rest/roles/:id -> create a new role.
     */
    @RequestMapping(value = "/role",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void create(@RequestBody Role role){
        super.create(role);
    }

    /**
     * POST  /rest/role -> update role
     */
    @RequestMapping(value = "/role",
            method = RequestMethod.PUT,
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

    /**
     * GET  /rest/roles/:id -> get all the roles.
     */
    @RequestMapping(value = "/role",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public  AppPage<Role> getAll(AppPageable appPageable){
        return super.getAll(appPageable);
    }

    /**
     * GET  /rest/roles -> get the "name" role.
     */
    @RequestMapping(value = "/role/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Role> get(@PathVariable Long id, HttpServletResponse response){
       return super.get(id, response);
    }

    /**
     * DELETE   /rest/roles/:id -> delete the "name" role
     */
    @RequestMapping(value = "/role/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable Long id){
        super.delete(id);
    }

}

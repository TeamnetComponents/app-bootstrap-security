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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    @RequestMapping(value = "/{id}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> updateById(@PathVariable Long id,@RequestBody RoleDTO roleDTO) {
        log.debug("REST request to update the role : {}", id);
        Boolean roleFound = roleService.updateRoleById(id, roleDTO);
        if (roleFound == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Override
    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Role> get(@PathVariable Long id) {
        log.debug("REST request to get  : {}", id);
        Role role = roleService.getOneById((Long) id);
        if (role == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(role, HttpStatus.OK);
    }
}

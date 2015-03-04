#Spring security data model and method annotation configuration module

## Preface

### Introduction

This is a separate module that implements a security data model and configures a custom MethodSecurityExpressionHandler

### Context

#### Model

A graphic data model for the security user and authorities is the following:

![model](http://git-components.teamnet.ro/tree/components%2Fjava%2Fapp-security.git/Security_Model.jpg)

#### Method Annotation

The CustomGlobalSecurityConfiguration bean from the AdditionalSpringSecurityConfiguration class, annotated with
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true) lets us annotate custom method in the following way:

``` java

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public AppPage<Module> findAll(AppPageable appPageable) {
        return moduleRepository.findAll(appPageable);
    }

```

#### Security Filter

The SecurityAccessFilter class is used to intercept all request mapped like /rest/entityName or /rest/menuName and verifies
if the current user has access to the accessed menu or entity.

### Usage

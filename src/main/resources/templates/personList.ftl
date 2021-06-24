<#import "parts/common.ftl" as common>
<@common.page>
    <div class="card text-field dark">
        <div class="card-header">
            <ul class="nav nav-tabs card-header-tabs">
                <#--                personsByCategories-->
                <#list categories as category>
                    <li class="nav-item">
                        <a aria-current="true"
                           class="nav-link <#if category == activeCategory>active</#if>"
                           href="/person?activeCategory=${category}">${category}
                        </a>
                    </li>
                </#list>
            </ul>
        </div>
        <div class="card-body">




            <#list persons as person>
                <div class="d-flex flex-row me-auto">
                    <div class="d-flex flex-row me-auto">
                        <div class="d-flex align-items-center">
                            <a class="navbar-link " href="person/${person.id}">
                                <div class="avatar-circle s-list-item primary">
                                    <span class="initials">${person.initials}</span>
                                </div>
                            </a>
                        </div>
                        <div class="mx-3">
                            <a class="navbar-link" href="person/${person.id}">
                                <h5 class="text-dark">${person.getFull_name()}</h5>
                            </a>

                            <p class="card-text">${person.email}</p>
                        </div>
                    </div>
                    <div class="">
                        <a class="navbar-link text-secondary" href="person/${person.id}/edit">Редактировать</a>
                    </div>
                </div>
                <#if !person?is_last>
                    <hr style="height: 1px; border: 0 solid  rgba(100,100,100,100.125); border-top-width: 1px; margin-left: 65px;"  />
                </#if>




            </#list>
        </div>
    </div>
</@common.page>

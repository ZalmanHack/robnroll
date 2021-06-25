<#import "avatar.ftl" as avatar>

<#macro personItem person>
    <div class="d-flex flex-row me-auto">
        <a class="navbar-link d-flex flex-row me-auto" href="person/${person.id}">
            <div class="d-flex align-items-center">
                <@avatar.page "${person.profile_pic!''}" "${person.initials}" "primary" "s-list-item"/>
            </div>
            <div class="mx-3">
                <h5 class="text-dark">${person.getFull_name()}</h5>
                <p class="text-dark card-text">${person.email}</p>
            </div>
        </a>
        <div class="">
            <a class="navbar-link text-secondary" href="person/${person.id}/edit">Редактировать</a>
        </div>
    </div>
</#macro>

<#macro brigadeItem brigade link>
    <div class="d-flex flex-row me-auto">
        <a class="navbar-link d-flex flex-row me-auto" href="${link}">
            <div class="d-flex align-items-center">
                <@avatar.page "" "${brigade.initials}" "danger" "s-list-item"/>
            </div>
            <div class="mx-3">
                <h5 class="text-dark">${brigade.getName()}</h5>
                <p class="text-dark card-text">${brigade.description!""}</p>
            </div>
        </a>
        <div class="">
            <a class="navbar-link text-secondary" href="brigade/${brigade.id}/edit">Редактировать</a>
        </div>
    </div>
</#macro>
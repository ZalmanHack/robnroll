<#include "security.ftl">
<#import "avatar.ftl" as avatar>

<nav class="navbar navbar-expand-lg navbar-light bg-light fixed-top">
    <div class="container">
        <a class="navbar-brand" href="/">Rob & Roll</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">

            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <#if know>
                    <li class="nav-item">
                        <a class="nav-link" href="/">Главная</a>
                    </li>
                    <#if isAdmin>
<#--                        <li><hr class="me-5"></li>-->
                        <li class="nav-item">
                            <a class="nav-link" href="/person">Пользователи</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="/brigade">Бригады</a>
                        </li>
                    </#if>
                </#if>
            </ul>


            <a class="navbar-link pe-2 d-inline-block" href="/person/${id}">
                <div class="navbar-text">${first_name}</div>
            </a>
            <a class="navbar-link d-inline-block" href="/person/${id}">
                <@avatar.page "${profile_pic}" "${initials}" "primary" "s-navbar"/>
            </a>


            <#if know>
                <a href="/logout" class="btn btn-outline-danger ms-3">Выход</a>
            <#else>
                <a href="/login" class="btn btn-primary ms-3">Вход</a>
                <a href="/registration" class="btn btn-outline-primary ms-3">Регистрация</a>
            </#if>
        </div>
    </div>
</nav>
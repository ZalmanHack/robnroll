<#import "parts/common.ftl" as common>
<#import "parts/login.ftl" as login>
<#import "parts/navbar.ftl" as navbar>

<@common.page>
    <@login.login "/login" "Вход" false/>
</@common.page>
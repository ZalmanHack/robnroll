<#assign
know = Session.SPRING_SECURITY_CONTEXT??
>

<#if know>
    <#assign
    person = Session.SPRING_SECURITY_CONTEXT.authentication.principal
    first_name = person.getFirst_name()
    initials = person.getInitials()
    isAdmin = person.isAdmin()
    >
<#else>
    <#assign
    first_name = "Гость"
    initials = "ГС"
    isAdmin = false
    >
</#if>

<#assign
know = Session.SPRING_SECURITY_CONTEXT??
>

<#if know>
    <#assign
    person = Session.SPRING_SECURITY_CONTEXT.authentication.principal
    first_name = person.getFirst_name()
    initials = person.getInitials()
    profile_pic = person.getProfile_pic()
    isAdmin = person.isAdmin()
    id = person.getId()
    >
<#else>
    <#assign
    profile_pic = ""
    first_name = "Гость"
    initials = "ГС"
    isAdmin = false
    id = 0
    >
</#if>

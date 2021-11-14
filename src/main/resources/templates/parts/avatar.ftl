<#macro page profile_pic initials color size>
    <div class="avatar-circle ${color} ${size}">
        <#if profile_pic != "">
            <img class="avatar-circle" src="/img/${profile_pic}">
        <#else>
            <span class="initials"> <#if initials??>${initials}<#else>ГС</#if></span>
        </#if>
    </div>
</#macro>
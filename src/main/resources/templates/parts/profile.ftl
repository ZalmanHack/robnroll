<#import "avatar.ftl" as avatar>

<#macro page person isEditable>
    <@avatar.page "${person.profile_pic!''}" "${person.initials}" "primary" "s-profile"/>
</#macro>
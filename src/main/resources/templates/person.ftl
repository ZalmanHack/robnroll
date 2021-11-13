<#import "parts/common.ftl" as common>
<#import "parts/profile.ftl" as profile>

<@common.page>
    <@profile.page "/person/${person.id}" person categories activeCategory false filter_name/>
</@common.page>

<#import "parts/common.ftl" as common>
<#import "parts/personForm.ftl" as personForm>

<@common.page>
    <@personForm.show person categories activeCategory filter_name/>
</@common.page>

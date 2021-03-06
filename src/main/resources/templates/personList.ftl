<#import "parts/common.ftl" as common>
<#import "parts/cardHeader.ftl" as cardHeader>
<#import "parts/item.ftl" as item>

<@common.page>
    <div class="card text-field dark ">
        <@cardHeader.header "/person" categories activeCategory "Поиск еще не работает" ""/>
        <div class="card-body">
            <#list persons as person>
                <@item.personItem "person/${person.id}" person true/>
                <#if !person?is_last>
                    <hr style="height: 1px; border: 0 solid  rgba(100,100,100,100.125); border-top-width: 1px; margin-left: 65px;"/>
                </#if>
            <#else>
                Список пуст
            </#list>
        </div>
    </div>
</@common.page>

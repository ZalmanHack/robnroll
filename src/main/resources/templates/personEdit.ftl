<#import "parts/common.ftl" as common>
<#import "parts/profile.ftl" as profile>

<@common.page>
<#--    <@profile.page "/person/${person.id}/save" person categories activeCategory true/>-->


    <form action="/person/${person.id}/save" method="post" enctype="multipart/form-data">
        <input type="text" value="${person.username}" name="username">
        <input type="email" value="${person.email}" name="email">

        <#list roles as role>
            <div>
                <label><input type="checkbox" name="${role}" ${person.roles?seq_contains(role)?string("checked","")}>${role}</label>
            </div>
        </#list>

        <div>
            <select size="1" name="brigade">
                <option selected value="">Выберите бригаду</option>
                <#list brigades as brigade>
                    <option <#if brigade == person.brigade!''>selected</#if> value="${brigade.id}">${brigade.name}</option>
                </#list>
            </select>
        </div>

        <input type="file" name="profile_pic">
        <input type="hidden" value="${person.id}" name="personId">
        <input type="hidden" value="${_csrf.token}" name="_csrf">
        <button type="submit">Сохранить</button>
    </form>
    <form action="/person/${person.id}/delete" method="post">
        <input type="hidden" value="${person.id}" name="personId">
        <input type="hidden" value="${_csrf.token}" name="_csrf">
        <button type="submit">Удалить</button>
    </form>
</@common.page>

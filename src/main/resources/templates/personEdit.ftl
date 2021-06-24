<#import "parts/common.ftl" as common>

<@common.page>
    <div>
        <#if person.profile_pic??>
            <img src="/img/${person.profile_pic}" width="200">
        </#if>
    </div>

    <form action="/person/${person.id}/save" method="post" enctype="multipart/form-data">
        <input type="text" value="${person.username}" name="username">
        <input type="email" value="${person.email}" name="email">

        <#list roles as role>
            <div>
                <label><input type="checkbox" name="${role}" ${person.roles?seq_contains(role)?string("checked","")}>${role}</label>
            </div>
        </#list>

        <div>
            <select size="1" name="brigadeId">
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

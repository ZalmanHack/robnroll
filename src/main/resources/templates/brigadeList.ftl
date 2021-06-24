<#import "parts/common.ftl" as common>
<#import "parts/login.ftl" as login>

<@common.page>
    <@login.logout />
    <span><a href="/person">Пользователи</a></span>
    <div>
        <form method="post" action="/brigade">
            <input type="text" name="name" placeholder="Введите название бригады"/>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <button type="submit">Добавить</button>
        </form>

        <form method="get" action="/brigade">
            <#if filter_name??>
                <input type="text" name="filter_name" placeholder="Поиск бригады" value="${filter_name}">
            <#else>
                <input type="text" name="filter_name" placeholder="Поиск бригады" value="">
            </#if>
            <button type="submit">Найти</button>
        </form>
    </div>


    <div>Список бригад</div>
    <#list brigades as brigade>
        <div>
            <i>${brigade.id}</i>
            <span>${brigade.name}</span>
        </div>
    <#else>
        Нет сообщений
    </#list>


</@common.page>
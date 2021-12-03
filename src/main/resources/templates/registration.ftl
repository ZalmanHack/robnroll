<#import "parts/common.ftl" as common>
<#import "parts/authForm.ftl" as authForm>

<@common.page>
    <div class="mx-auto" style="width: 300px">
        <div class="text-center pb-3">
            <h2>Регистрация</h2>
        </div>
        <form action="/registration" method="post">
            <@authForm.messages/>
            <@authForm.registration/>
            <div class="d-flex flex-row-reverse mb-3">
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <button type="submit" class="btn btn-primary ms-3 px-3">Продолжить</button>
                <a class="btn btn-outline-primary ms-3 px-3" href="/logout">Отмена</a>
            </div>
        </form>
    </div>
</@common.page>
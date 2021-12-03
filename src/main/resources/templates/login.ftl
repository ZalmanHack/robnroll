<#import "parts/common.ftl" as common>
<#import "parts/authForm.ftl" as authForm>

<@common.page>
    <div class="mx-auto" style="width: 300px">
        <div class="text-center pb-3">
            <h2>Вход</h2>
        </div>
        <form action="/login" method="post">
            <@authForm.messages/>
            <@authForm.login/>
            <div class="d-flex flex-row-reverse mb-3">
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <button type="submit" class="btn btn-primary ms-3 px-3">Вход</button>
                <a class="btn btn-outline-primary ms-3 px-3" href="/registration">Регистрация</a>
            </div>
        </form>
    </div>
</@common.page>
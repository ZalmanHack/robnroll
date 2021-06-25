<#macro login path pageName isRegistration>
    <div class="text-center pb-3">
        <h2>${pageName}</h2>
    </div>
    <#if info_message??>
        <div class="alert alert-primary text-center pb-3" role="alert">
            ${info_message}
        </div>
    </#if>
    <#if error_message??>
        <div class="alert alert-danger text-center pb-3" role="alert">
            ${error_message}
        </div>
    </#if>
    <#if success_message??>
        <div class="alert alert-success text-center pb-3" role="alert">
            ${success_message}
        </div>
    </#if>
    <form action="${path}" method="post" class="mx-auto" style="width: 300px;">
        <div class="mb-3">
            <label for="inputUsername" class="form-label">Логин</label>
            <input name="username"
                   type="text"
                   value="<#if person??>${person.username}</#if>"
                   class="form-control ${(usernameError??)?string("is-invalid","")}"
                   id="inputUsername"
                   aria-describedby="usernameHelp">
            <div id="usernameHelp" class="form-text">Возможно, мы никому не отдадим ваши данные :)
            </div>
            <#if usernameError??>
                <div class="invalid-feedback">
                    ${usernameError}
                </div>
            </#if>
        </div>

        <#if isRegistration>
            <div class="mb-3">
                <label for="inputFirst_name" class="form-label">Имя</label>
                <input name="first_name"
                       type="text"
                       value="<#if person??>${person.first_name}</#if>"
                       class="form-control ${(first_nameError??)?string("is-invalid","")}"
                       id="inputFirst_name">
                <#if first_nameError??>
                    <div class="invalid-feedback">
                        ${first_nameError}
                    </div>
                </#if>
            </div>


            <div class="mb-3">
                <label for="inputLast_name" class="form-label">Фамилия</label>
                <input name="last_name"
                       type="text"
                       value="<#if person??>${person.last_name}</#if>"
                       class="form-control ${(last_nameError??)?string("is-invalid","")}"
                       id="inputLast_name">
                <#if last_nameError??>
                    <div class="invalid-feedback">
                        ${last_nameError}
                    </div>
                </#if>
            </div>





            <div class="mb-3">
                <label for="inputEmail"
                       class="form-label">Почта</label>
                <input name="email"
                       type="text"
                       value="<#if person??>${person.email}</#if>"
                       class="form-control ${(emailError??)?string("is-invalid","")}"
                       id="inputEmail"
                       aria-describedby="emailHelp">
                <div id="emailHelp" class="form-text">К данной почте будут привязаны Ваши учетные данные</div>
                <#if emailError??>
                    <div class="invalid-feedback">
                        ${emailError}
                    </div>
                </#if>
            </div>
        </#if>
        <div class="mb-3">
            <label for="inputPassword" class="form-label">Пароль</label>
            <input name="password" type="password"
                   class="form-control ${(passwordError??)?string("is-invalid","")}"
                   id="inputPassword">
            <#if passwordError??>
                <div class="invalid-feedback">
                    ${passwordError}
                </div>
            </#if>
        </div>
        <#if isRegistration>
            <div class="mb-3">
                <label for="inputPassword" class="form-label">Повторите пароль</label>
                <input name="password_2" type="password"
                       class="form-control ${(password_2Error??)?string("is-invalid","")}"
                       id="inputPassword"
                       aria-describedby="passwordHelp">
                <div id="passwordHelp" class="form-text">Повторите ввод Вашего пароля</div>
                <#if password_2Error??>
                    <div class="invalid-feedback">
                        ${password_2Error}
                    </div>
                </#if>
            </div>
        </#if>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <div class="d-flex flex-row-reverse mb-3">
            <#if !isRegistration>
                <button type="submit" class="btn btn-primary ms-3 px-3">Вход</button>
                <a class="btn btn-outline-primary ms-3 px-3" href="/registration">Регистрация</a>
            <#else>
                <button type="submit" class="btn btn-primary ms-3 px-3">Продолжить</button>
                <a class="btn btn-outline-primary ms-3 px-3" href="/logout">Отмена</a>
            </#if>
        </div>
    </form>
</#macro>

<#macro logout>
    <div>
        <form action="/logout" method="post">
            <input type="submit" value="Sign Out"/>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        </form>
    </div>
</#macro>

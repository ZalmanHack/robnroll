<#include "security.ftl">
<#import "avatar.ftl" as avatar>
<#import "cardHeader.ftl" as cardHeader>
<#import "item.ftl" as item>

<#macro page path person categories activeCategory isEditable filter_name>

    <div class="d-flex flex-row me-auto mt-4 mb-3">
        <div class="navbar-link d-flex flex-row me-auto">
            <div class="align-items-top">
                <@avatar.page "${person.profile_pic!''}" "${person.initials}" "primary" "s-profile"/>
            </div>
            <div class="mx-5">
                <#if !isEditable>
                    <h3 class="text-dark">${person.getFull_name()}</h3>
                    <div><small class="text-muted card-text">Почта</small></div>
                    <div><span class="text-dark card-text">${person.email}</span></div>
                    <div><small class="text-muted card-text">Логин</small></div>
                    <div><span class="text-dark card-text">${person.username}</span></div>
                    <div><small class="text-muted card-text">Бригада</small></div>
                    <div>
                        <span class="text-dark card-text"><#if person.brigade??>${person.brigade.name}<#else>Бригада не выбрана</#if></span>
                    </div>
                <#else >





                    <form action="${path}" method="post" class="mx-auto" enctype="multipart/form-data">
                        <#--                    -->
                        <div class="mb-3">
                            <label for="inputFirst_name" class="form-label">Имя</label>
                            <input name="first_name"
                                   type="text"
                                   value="<#if person??>${person.first_name}</#if>"
                                   class="form-control ${(first_nameError??)?string("is-invalid","")}"
                                   id="inputFirst_name" ${(!isAdmin)?then("readonly","")}>
                            <#if first_nameError??>
                                <div class="invalid-feedback">
                                    ${first_nameError}
                                </div>
                            </#if>
                        </div>
                        <#--                    -->

                        <div class="mb-3">
                            <label for="inputLast_name" class="form-label">Фамилия</label>
                            <input name="last_name"
                                   type="text"
                                   value="<#if person??>${person.last_name}</#if>"
                                   class="form-control ${(last_nameError??)?string("is-invalid","")}"
                                   id="inputLast_name" ${(!isAdmin)?then("readonly","")}>
                            <#if last_nameError??>
                                <div class="invalid-feedback">
                                    ${last_nameError}
                                </div>
                            </#if>
                        </div>

                        <#--                    -->
                        <div class="mb-3">
                            <label for="inputEmail"
                                   class="form-label">Почта</label>
                            <input name="email"
                                   type="text"
                                   value="<#if person??>${person.email}</#if>"
                                   class="form-control ${(emailError??)?string("is-invalid","")}"
                                   id="inputEmail"
                                   aria-describedby="emailHelp">
                            <div id="emailHelp" class="form-text">К данной почте будут привязаны Ваши учетные данные
                            </div>
                            <#if emailError??>
                                <div class="invalid-feedback">
                                    ${emailError}
                                </div>
                            </#if>
                        </div>

                        <#--                    -->
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

                        <#--                    -->
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
                        <#--                    -->
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
                        <#--                    -->
                        <div>
                            <select size="1" name="brigade">
                                <option selected value="">Выберите бригаду</option>
                                <#list brigades as brigade>
                                    <option <#if brigade == person.brigade!''>selected</#if>
                                            value="${brigade.id}">${brigade.name}</option>
                                </#list>
                            </select>
                        </div>
                        <#--                        -->

                        <div class="mb-3">
                            <input type="file" name="profile_pic">
                        </div>

                        <div class="d-flex flex-row-reverse mb-3">
                            <input type="hidden" value="${_csrf.token}" name="_csrf">
                            <button type="submit" class="btn btn-primary ms-3 px-3">Сохранить</button>
                            <a class="btn btn-outline-primary ms-3 px-3" href="/person/${person.id}">Отмена</a>
                        </div>
                    </form>
                </#if>
            </div>
        </div>

        <#if !isEditable && (person.id == id || isAdmin)>
            <div>
                <a class="navbar-link text-secondary" href="${person.id}/edit">Редактировать</a>
            </div>
        </#if>
    </div>

    <#if !isEditable>
        <div class="navbar-link d-flex flex-row me-auto">
            <#list person.roles as role>
                <#if role == "ADMIN">
                    <span class="badge bg-danger me-3 my-3 ">Управляющий</span>
                <#elseif role == "USER">
                    <span class="badge bg-primary me-3 my-3 ">Исполнитель</span>
                </#if>
            </#list>
        </div>

        <div class="card text-field dark ">
            <@cardHeader.header "/person" categories activeCategory "Имя пользователя" filter_name/>
            <div class="card-body">
                <#if persons??>
                    <#list persons as person>
                        <@item.personItem "${person.id}" person false/>
                        <#if !person?is_last>
                            <hr style="height: 1px; border: 0 solid  rgba(100,100,100,100.125); border-top-width: 1px; margin-left: 65px;"/>
                        </#if>
                    <#else>
                        Нет бригады
                    </#list>
                </#if>
            </div>
        </div>
    </#if>

</#macro>
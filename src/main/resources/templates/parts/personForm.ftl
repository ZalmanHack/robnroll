<#include "security.ftl">
<#import "avatar.ftl" as avatar>
<#import "cardHeader.ftl" as cardHeader>
<#import "item.ftl" as item>
<#import "authForm.ftl" as authForm>

<#macro show person categories activeCategory filter_name>

    <div class="d-flex flex-row me-auto mt-4 mb-3">
        <div class="navbar-link d-flex flex-row me-auto">
            <div class="align-items-top">
                <@avatar.page "${person.profile_pic!''}" "${person.initials}" "primary" "s-profile"/>
            </div>
            <div class="mx-5">
                <h3 class="text-dark">${person.getFull_name()}</h3>
                <div><small class="text-muted card-text">Почта</small></div>
                <div><span class="text-dark card-text">${person.email}</span></div>
                <div><small class="text-muted card-text">Логин</small></div>
                <div><span class="text-dark card-text">${person.username}</span></div>
                <div><small class="text-muted card-text">Бригада</small></div>
                <div>
                    <span class="text-dark card-text"><#if person.brigade??>${person.brigade.name}<#else>Бригада не выбрана</#if></span>
                </div>
            </div>
        </div>

        <#if person.id == id || isAdmin>
            <div>
                <a class="navbar-link text-secondary" href="${person.id}/edit">Редактировать</a>
            </div>
        </#if>
    </div>

    <div class="navbar-link d-flex flex-row me-auto">
        <#list person.roles as role>
            <#if role == "ADMIN">
                <span class="badge bg-danger me-3 my-3 ">Управляющий</span>
            <#elseif role == "USER">
                <span class="badge bg-primary me-3 my-3 ">Исполнитель</span>
            </#if>
        </#list>
    </div>

    <div class="">
        <canvas id="chartStat"></canvas>
    </div>

    <div class="card text-field dark ">
        <@cardHeader.header "/person" categories activeCategory "Почта пользователя" filter_name/>
        <div class="card-body" id="card-body">
<#--            <#if persons??>-->
<#--                <#list persons as person>-->
<#--                    <@item.personItem "${person.id}" person false/>-->
<#--                    <#if !person?is_last>-->
<#--                        <hr style="height: 1px; border: 0 solid  rgba(100,100,100,100.125); border-top-width: 1px; margin-left: 65px;"/>-->
<#--                    </#if>-->
<#--                <#else>-->
<#--                    Список пуст-->
<#--                </#list>-->
<#--            </#if>-->
        </div>
    </div>

</#macro>




<#macro edit person>
    <div class="d-flex flex-row me-auto mt-4 mb-3">
        <div class="navbar-link d-flex flex-row me-auto">
            <div class="align-items-top">
                <div class="mb-3">
                    <@avatar.page "${person.profile_pic!''}" "${person.initials}" "primary" "s-profile"/>
                </div>

                <#if isAdmin>
                    <label for="fromRoles" class="form-label">Выданные роли</label>
                    <div id="fromRoles">
                        <#list roles as role>
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" value=""
                                       id="${role}" name="${role}"
                                        ${person.roles?seq_contains(role)?string("checked","")}>
                                <label class="form-check-label" for="${role}">
                                    ${role}
                                </label>
                            </div>
                        </#list>
                    </div>
                </#if>

            </div>
            <div class="mx-5">
                <div class="mb-3">
                    <label for="fromBrigade" class="form-label">Бригада</label>
                    <select class="form-select" size="1" id="fromBrigade" name="brigade" ${isAdmin?then("","disabled")}>
                        <option selected value="">Не выбрана</option>
                        <#list brigades as brigade>
                            <option <#if brigade == person.brigade!''>selected</#if>
                                    value="${brigade.id}">${brigade.name}
                            </option>
                        </#list>
                    </select>
                </div>

                <div class="mb-3">
                    <label for="formFile" class="form-label">Загрузить аватар</label>
                    <input class="form-control ${(raw_profile_picError??)?string("is-invalid","")}"
                           type="file" id="formFile" name="raw_profile_pic">
                    <#if raw_profile_picError??>
                        <div class="invalid-feedback">
                            ${raw_profile_picError}
                        </div>
                    </#if>
                </div>






                <!-- <@authForm.registration/> -->



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

                <div class="mb-3">
                    <label for="inputPassword1" class="form-label">Повторите пароль</label>
                    <input name="password_1" type="password"
                           class="form-control ${(password_1Error??)?string("is-invalid","")}"
                           id="inputPassword1">
                    <#if password_1Error??>
                        <div class="invalid-feedback">
                            ${password_1Error}
                        </div>
                    </#if>
                </div>

                <div class="mb-3">
                    <label for="inputPassword2" class="form-label">Повторите пароль</label>
                    <input name="password_2" type="password"
                           class="form-control ${(password_2Error??)?string("is-invalid","")}"
                           id="inputPassword2"
                           aria-describedby="passwordHelp">
                    <div id="passwordHelp" class="form-text">Повторите ввод Вашего пароля</div>
                    <#if password_2Error??>
                        <div class="invalid-feedback">
                            ${password_2Error}
                        </div>
                    </#if>
                </div>



                <#nested/>
            </div>
        </div>
    </div>
</#macro>
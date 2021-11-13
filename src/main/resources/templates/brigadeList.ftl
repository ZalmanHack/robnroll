<#import "parts/common.ftl" as common>
<#import "parts/authForm.ftl" as login>
<#import "parts/cardHeader.ftl" as cardHeader>
<#import "parts/item.ftl" as item>

<@common.page>

    <div class="d-flex justify-content-center pb-3">
        <button class="btn btn-outline-primary" type="button" data-bs-toggle="collapse"
                data-bs-target="#collapseExample" aria-expanded="false" aria-controls="collapseExample">
            Создать бригаду
        </button>
    </div>

    <div class="collapse <#if nameError??>show</#if>" id="collapseExample">
        <form action="/brigade" method="post" class="mx-auto" style="width: 300px;">
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <div class="card card-body mb-3">
                <div class="mb-3">
                    <label for="inputName" class="form-label">Название бригады</label>
                    <input name="name"
                           type="text"
                           class="form-control ${(nameError??)?string("is-invalid","")}"
                           id="inputName"
                    >
                    <#if nameError??>
                        <div class="invalid-feedback">
                            ${nameError}
                        </div>
                    </#if>
                </div>
                <div class="mb-3">
                    <label for="textDescription" class="form-label">Описание</label>
                    <textarea name="description"
                              class="form-control"
                              id="textDescription"
                              rows="3"></textarea>
                </div>
                <div class="d-flex flex-row-reverse">
                    <button type="submit" class="btn btn-success ms-3 px-3">Создать</button>
                </div>
            </div>
        </form>
    </div>


    <div class="card text-field dark ">
        <#if brigades??>
            <@cardHeader.header "/brigade" categories activeCategory "Название бригады" filter_name/>
            <#elseif persons??>
            <@cardHeader.header "/brigade" categories activeCategory "Почта" filter_name/>
            <#else>
            <@cardHeader.header "/brigade" categories activeCategory "" filter_name/>
        </#if>
        <div class="card-body">
            <#if brigades??>
                <#list brigades as brigade>
                    <@item.brigadeItem brigade "/brigade?activeCategory=${brigade.name}"/>
                    <#if !brigade?is_last>
                        <hr style="height: 1px; border: 0 solid  rgba(100,100,100,100.125); border-top-width: 1px; margin-left: 65px;"/>
                    </#if>
                <#else>
                    Нет бригад
                </#list>
            <#elseif persons??>
                <#list persons as person>
                    <@item.personItem "person/${person.id}" person true/>
                    <#if !person?is_last>
                        <hr style="height: 1px; border: 0 solid  rgba(100,100,100,100.125); border-top-width: 1px; margin-left: 65px;"/>
                    </#if>
                <#else>
                    Нет пользователей
                </#list>

            </#if>
        </div>
    </div>










</@common.page>
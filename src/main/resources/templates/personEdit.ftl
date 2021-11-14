<#import "parts/common.ftl" as common>
<#import "parts/personForm.ftl" as profile>

<@common.page>

        <form action="/person/${person.id}/save" method="post" class="mx-auto" enctype="multipart/form-data">
                <@profile.edit person>
                        <div class="d-flex flex-row-reverse mb-3">
                                <input type="hidden" value="${_csrf.token}" name="_csrf">
                                <input type="hidden" value="${person.id}" name="id">
                                <button type="submit" class="btn btn-primary ms-3 px-3">Сохранить</button>
                                <a class="btn btn-outline-primary ms-3 px-3" href="/person/${person.id}">Отмена</a>
                        </div>
                </@profile.edit>
        </form>

</@common.page>

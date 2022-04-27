$(() => {
    $(".complete-form").find("input[type='checkbox']").on("change", (e) => {
        const status = $(e.currentTarget).is(":checked");
        const form = $(e.currentTarget).closest("form");
        form.submit();
    });

    $(".delete-form").find(".delete").on("click", (e) => {
        const answer = confirm("Are you sure you would like to delete this task?");
        if (answer) {
            const form = $(e.currentTarget).closest("form");
            form.submit();
        }
    });

    $(".logout-form").find(".logout").on("click", (e) => {
        const form = $(e.currentTarget).closest("form");
        form.submit();
    });
});
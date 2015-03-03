function previewImage(input, img) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();
            
        reader.onload = function (e) {
            img.attr('src', e.target.result);
        }
            
        reader.readAsDataURL(input.files[0]);
    }
}

$("document").ready(function(){
    $("#form\\:imgShowroom").change(function(){
        previewImage(this, $("#form\\:thumbShowroom"));
    });
    
    $("#form\\:logo").change(function(){
        previewImage(this, $("#form\\:thumbLogo"));
    });
});


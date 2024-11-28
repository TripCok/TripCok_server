let selectedCategories = [];

/* 카테고리 드롭다운 */
document.querySelectorAll('.dropdown-item').forEach(item => {
    item.addEventListener('click', function (event) {
        event.preventDefault(); // 페이지 이동 방지
        const categoryId = this.getAttribute('data-id')
        const categoryName = this.textContent.trim();

        if (!selectedCategories.includes(categoryId)) {
            selectedCategories.push(categoryId);

            /* 선택된 카테고리 업데이트 */
            const selectLabel = document.getElementById('CategoriesLabel')
            selectLabel.textContent = selectedCategories
                .map(id => document.querySelector('.dropdown-item[data-id="${id}"]').textContent.trim())
                .join(', ');

            console.log('선택된 카테고리:', selectedCategories);
        }
    });
});

// 카테고리 이름 -> ID 매핑 (실제 ID 값으로 변경)
function getCategoryID(categoryName) {
    const categoryMap = {
        '힐링': 1,
        '자연': 2,
        '캠핑': 3
    };
    return categoryMap[categoryName];
}

/* 이미지 업로드 */
document.getElementById("addPhotoIcon").addEventListener("click", function () {
    document.getElementById("placeImages").click();
});

document.getElementById("placeImages").addEventListener("change", function () {
    const fileList = this.files;
    const photoCards = document.querySelector(".add-photo-cards");

    // 기존 파일 목록 초기화
    photoCards.innerHTML = "";

    // 선택된 파일들 표시
    Array.from(fileList).forEach((file, index) => {
        const fileCard = document.createElement("div");
        fileCard.className = "add-photo-card";
        fileCard.innerHTML = `
            <div>${index + 1}</div>
            <div>${file.name}</div>
        `;
        photoCards.appendChild(fileCard);
    });
});
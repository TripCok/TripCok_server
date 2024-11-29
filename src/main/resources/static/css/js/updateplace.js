document.querySelectorAll('.dropdown-toggle').forEach(dropdown => {
    dropdown.addEventListener('click', function (e) {
        const menu = this.nextElementSibling;
        menu.style.display = menu.style.display === 'block' ? 'none' : 'block';
    });

    document.addEventListener('click', function (e) {
        if (!dropdown.contains(e.target)) {
            const menu = dropdown.nextElementSibling;
            if (menu) menu.style.display = 'none';
        }
    });
});

// 카테고리 목록을 서버에서 받아오는 함수
async function loadCategories() {
    try {
        const response = await fetch("/api/v1/place/category/all"); // 카테고리 API 경로 (서버에서 제공)
        if (response.ok) {
            const categories = await response.json();
            populateCategoryDropdown(categories);
        } else {
            alert("카테고리 로드 실패");
        }
    } catch (error) {
        console.error("Error loading categories: ", error);
        alert("카테고리 로드 중 오류가 발생했습니다.");
    }
}

// 카테고리 드롭다운에 데이터를 삽입하는 함수
function populateCategoryDropdown(categories) {
    const dropdownMenu = document.querySelector(".dropdown-menu");
    dropdownMenu.innerHTML = ""; // 기존 항목들 제거

    // 카테고리 목록을 순회하여 드롭다운에 항목을 추가
    categories.forEach(category => {
        const li = document.createElement("li");
        li.innerHTML = `<a class="dropdown-item" href="#" data-id="${category.id}">${category.name}</a>`;
        dropdownMenu.appendChild(li);
    });

    // 카테고리 클릭 이벤트 리스너 추가
    document.querySelectorAll(".dropdown-item").forEach(item => {
        item.addEventListener("click", function () {
            const categoryId = item.getAttribute("data-id");
            const categoryName = item.innerText;
            document.getElementById("CategoriesLabel").innerText = categoryName;
            selectedCategories = [categoryId]; // 선택된 카테고리 ID 업데이트
        });
    });
}

// 페이지 로드 시 카테고리 목록을 불러오는 함수 호출
document.addEventListener("DOMContentLoaded", function () {
    loadCategories();
});

// 이미지 업로드 기능
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

// 업데이트 버튼 클릭 시 여행지 수정 API 호출
document.getElementById("updatePlaceButton").addEventListener("click", async function () {
    /* 여행지 수정 API에 전달할 데이터 수집 */
    const placeId = 1; // 예시: 수정할 여행지 ID (실제로는 페이지 URL이나 다른 방법으로 가져올 수 있음)
    const request = {
        placeName: document.getElementById("PlaceName").value,
        address: document.getElementById("Address").value,
        description: document.getElementById("Description").value,
        // 선택된 카테고리 ID 배열
        categoryIds: selectedCategories
    };

    /* 파일 업로드 데이터 */
    const fileInput = document.getElementById("placeImages");
    const files = fileInput.files;
    const formData = new FormData();

    /* 요청 데이터 */
    formData.append("request", new Blob([JSON.stringify(request)], { type: "application/json" }));

    // 파일 추가
    for (let i = 0; i < files.length; i++) {
        formData.append("file", files[i]);
    }

    try {
        const response = await fetch(`/api/vi/place/${placeId}`, {
            method: "PUT",
            body: formData,
        });

        if (response.ok) {
            alert('여행지가 성공적으로 수정되었습니다.');
            window.location.reload();
        } else {
            const error = await response.json();
            alert(`수정 실패: ${error.message}`);
        }
    } catch (error) {
        console.error('Error updating place: ', error);
        alert('여행지 수정 중 오류가 발생했습니다.');
    }
});

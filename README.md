# 📂 AI 파일 & 이미지 RAG 시스템 (PDF & Image RAG) 🚀

<!-- workspace-readme-learning:start -->
## 파일과 연결한 학습 안내

아래 설명은 이 폴더의 실제 소스와 빌드 설정을 기준으로 정리했습니다. 기존 소개의 기능 설명은 연결된 파일과 함께 확인할 수 있습니다.

### 주요 파일과 역할

| 파일 | 역할과 읽을 내용 |
| --- | --- |
| [build.gradle](<build.gradle>) | Gradle 플러그인·JDK·의존성과 빌드 작업 설정 |
| [src/main/java/org/example/aifile/AifileApplication.java](<src/main/java/org/example/aifile/AifileApplication.java>) | Spring Boot 애플리케이션 진입점 — `main` |
| [src/main/java/org/example/aifile/controller/ImageController.java](<src/main/java/org/example/aifile/controller/ImageController.java>) | 요청 매핑·입력 바인딩과 응답 처리 — `formPage`, `uploadImage`, `search` |
| [src/main/java/org/example/aifile/controller/PDFController.java](<src/main/java/org/example/aifile/controller/PDFController.java>) | 요청 매핑·입력 바인딩과 응답 처리 — `formPage`, `uploadPDF`, `rag` |
| [src/main/resources/templates/index.html](<src/main/resources/templates/index.html>) | Hello PDF & Image! 화면 |
| [src/main/java/org/example/aifile/service/ImageService.java](<src/main/java/org/example/aifile/service/ImageService.java>) | 업무 처리와 외부 의존성 호출 — `imageRagSearch`, `explain`, `resize` |
| [src/main/java/org/example/aifile/service/PDFService.java](<src/main/java/org/example/aifile/service/PDFService.java>) | 업무 처리와 외부 의존성 호출 — `ragChat`, `uploadPDF`, `validate` |
| [HELP.md](<HELP.md>) | 설계·학습·운영 내용을 설명하는 문서 |
| [settings.gradle](<settings.gradle>) | 프로젝트 구성 자료 |
| [src/main/java/org/example/aifile/config/ImageRagConfig.java](<src/main/java/org/example/aifile/config/ImageRagConfig.java>) | 빈 등록 또는 외부 설정 구성 — `primaryEmbeddingModel`, `imageVectorStore` |
| [src/main/java/org/example/aifile/dto/ImageRagSearchResult.java](<src/main/java/org/example/aifile/dto/ImageRagSearchResult.java>) | Java 타입과 동작 정의 — `ImageRagSearchResult` |
| [src/main/resources/templates/image/form.html](<src/main/resources/templates/image/form.html>) | Hello PDF & Image! 화면 |
| [src/main/resources/templates/pdf/form.html](<src/main/resources/templates/pdf/form.html>) | Hello PDF & Image! 화면 |
| [src/test/java/org/example/aifile/AifileApplicationTests.java](<src/test/java/org/example/aifile/AifileApplicationTests.java>) | 테스트 코드 |

### 실행과 설정 확인

- [build.gradle](<build.gradle>)의 플러그인과 의존성을 기준으로 구성합니다. 선언된 Java toolchain은 17입니다.
- Windows에서는 저장소 루트에서 `.\gradlew.bat bootRun`을 사용합니다.
- 환경 설정: [src/main/resources/application-ai.yaml](<src/main/resources/application-ai.yaml>), [src/main/resources/application-db.yaml](<src/main/resources/application-db.yaml>), [src/main/resources/application-file.yaml](<src/main/resources/application-file.yaml>), [src/main/resources/application.yaml](<src/main/resources/application.yaml>).
- 코드·설정에서 참조하는 환경 변수 이름: `DB_HOST`, `DB_NAME`, `DB_PASSWORD`, `DB_PORT`, `DB_USERNAME`, `GEMINI_API_KEY`, `SUPABASE_STORAGE_ACCESS_KEY`, `SUPABASE_STORAGE_ENDPOINT`, `SUPABASE_STORAGE_REGION`, `SUPABASE_STORAGE_SECRET_KEY`. 기본값과 필수 여부는 각 참조 위치에서 확인합니다.

### 관련 PDF와 보충 설명

- [7/30 강의](<../260629_ex/새 폴더/7-30/README.md>): 임베딩·청킹·VectorStore·검색 기반 답변을 연결합니다.
- [6/26 강의](<../260629_ex/새 폴더/6-26/README.md>): 예외 전달·자원 수명·파일 및 HTTP 입출력을 연결합니다.

이 링크는 구현을 이해하기 위한 관련 기초 자료입니다. 해당 강의가 이 저장소의 모든 기능이나 이후 버전의 API를 설명한다는 뜻은 아닙니다.

### 읽는 순서와 복습

- 문서 적재와 질문 처리를 나누고 검색된 근거가 모델 입력에 들어가는 위치를 읽습니다. 차원·모델 호환성, 빈 검색 결과, 중복 적재와 사용자별 문서 접근 범위를 확인합니다.
- 자원 생성 → 사용 → 실패 전달 → 정리 순으로 확인합니다. 파일 부재·문자 인코딩·외부 요청 실패를 구분하고 예외 원인이 보존되는지 확인합니다.

테스트 소스가 포함되어 있습니다. 이 문서 수정 작업에서는 애플리케이션·DB·외부 API 테스트를 실행하지 않았으므로 실행 결과를 보장하는 기록은 아닙니다.

<!-- workspace-readme-learning:end -->

이 프로젝트는 **Spring Boot**와 **Spring AI**, **Gemini (Google GenAI)**, 그리고 **Supabase (PostgreSQL + S3 Storage)**를 결합하여 만든 스마트 AI 시스템입니다. 

사용자가 올린 **PDF 문서**에서 답변을 찾거나, **이미지**를 해석하고 의미 기반으로 검색할 수 있는 기능을 제공합니다.

---

## 💡 아주 쉽게 이해하는 RAG (Retrieval-Augmented Generation)

> **"똑똑하지만 최근 정보나 내 개인 문서는 모르는 AI(교수님)에게 책(데이터베이스)을 찾아 전달해 주는 비서"**

* **일반적인 AI**: 엄청 공부를 많이 했지만, 사용자가 개인적으로 가지고 있는 PDF 파일이나 방금 올린 이미지 파일의 내용은 알지 못합니다.
* **RAG (검색 증강 생성)**: 
  1. 사용자가 올린 문서를 컴퓨터가 읽기 쉽게 잘게 쪼개서 보관합니다.
  2. 사용자가 질문("이 PDF에서 가장 중요한 내용이 뭐야?")을 던지면, 관련된 문서 조각들을 데이터베이스에서 먼저 찾아냅니다.
  3. 찾아낸 문서 조각들과 질문을 함께 AI에게 건네주며 **"여기 이 참고 문서들을 읽고 내 질문에 대답해줘"**라고 요청합니다.
  4. AI는 엉뚱한 거짓말(환각 현상)을 하지 않고, 주어진 참고 문서에 기반하여 정확한 답변을 제공합니다.

---

## 🛠️ 주요 핵심 기능

### 1. 📄 PDF RAG (문서 기반 스마트 질의응답)
* **문서 파싱**: PDF 파일을 업로드하면 `PagePdfDocumentReader`를 통해 페이지별로 텍스트를 읽어옵니다.
* **텍스트 쪼개기 (Token Splitting)**: 너무 긴 문서는 AI가 기억하기 힘듭니다. `TokenTextSplitter`를 사용해 약 800 토큰(단어 조각 단위) 크기로 의미 있게 분할합니다.
* **벡터 저장소 저장**: 쪼갠 텍스트 조각들의 '의미'를 숫자로 변환(임베딩)하여 PostgreSQL 데이터베이스(`PgVectorStore`)에 저장합니다.
* **컨텍스트 매칭 답변**: 질문이 들어오면 벡터 데이터베이스에서 의미가 가장 유사한 단락 3개(`topK(3)`)를 찾아냅니다. 이 단락들을 질문과 함께 Gemini AI 모델에 전달하여 신뢰성 높은 답변을 생성합니다.

### 2. 🖼️ 이미지 RAG & 클라우드 저장소 연동
* **이미지 리사이징**: 고화질 이미지는 용량이 커서 네트워크와 AI 처리 비용이 많이 듭니다. `Thumbnailator` 라이브러리를 사용하여 이미지를 512x512 크기로 압축하고 축소합니다.
* **이미지 캡셔닝 (AI 해석)**: 멀티모달 기능이 있는 Gemini AI 모델에 축소된 이미지를 보내 **"이 이미지가 어떤 내용인지 설명해줘"**라고 요청하여 이미지 설명(Caption)을 얻습니다.
* **Supabase Storage (S3) 업로드**: 원본 이미지를 UUID 기반의 중복 없는 파일명으로 변경하여 안전하게 Supabase Storage에 업로드합니다. 이후 웹 브라우저에서 바로 볼 수 있는 Public URL로 변환합니다.
* **이미지 의미 기반 검색**: 이미지 설명(Caption)과 이미지의 Public URL 링크를 함께 벡터 데이터베이스에 저장합니다. 사용자가 텍스트("귀여운 동물 사진")로 검색하면, 그 의미와 가장 잘 맞는 이미지를 찾아서 썸네일과 함께 화면에 보여줍니다.

---

## 🔍 서비스 흐름 보기

```mermaid
graph TD
    A[사용자 이미지/PDF 업로드] --> B(Java Backend)
    B -->|PDF 파싱 및 800토큰 분할| C[PGVector DB 저장]
    B -->|이미지 512px 축소| D[Gemini AI 이미지 해석]
    B -->|원본 이미지 업로드| E[Supabase Storage S3]
    D -->|생성된 설명과 이미지 주소 매핑| C
    
    F[사용자 검색/질문 입력] --> G(유사도 검색 top-3)
    C -->|관련성 높은 문서/이미지 전달| G
    G -->|컨텍스트 + 질문| H[Gemini AI 답변 생성 및 이미지 목록 반환]
```

---

## ⚙️ 실행 방법 및 주의사항

### 🔒 보안 주의 사항 (필수)
프로젝트 설정 값과 API Key들이 포함되는 **`.env.dev`** 파일은 중요한 개인 정보입니다.
* **절대 GitHub에 커밋하거나 타인에게 공유하지 마세요!**
* `.gitignore` 파일에 `.env*` 패턴이 등록되어 있어 안전하게 관리됩니다.

### 1. 환경 변수 파일 준비
프로젝트 루트 디렉토리에 **`.env.dev`** 파일을 생성하고 아래 양식에 맞추어 본인의 Key 정보를 채워 넣습니다:

```properties
# Supabase PostgreSQL Database 연결 설정
DB_HOST=your-supabase-db-host
DB_NAME=postgres
DB_USERNAME=your-db-username
DB_PASSWORD=your-db-password

# Google Gemini API Key (https://aistudio.google.com/apikey)
GEMINI_API_KEY=your-gemini-api-key

# Supabase Storage S3 설정 (Project Settings -> Storage)
SUPABASE_STORAGE_ENDPOINT=your-supabase-storage-s3-endpoint
SUPABASE_STORAGE_REGION=ap-northeast-2
SUPABASE_STORAGE_ACCESS_KEY=your-supabase-s3-access-key
SUPABASE_STORAGE_SECRET_KEY=your-supabase-s3-secret-key
```

### 2. 프로젝트 빌드 및 실행
```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```
서버가 켜지면 아래 주소로 접속하여 테스트할 수 있습니다:
* **PDF RAG 테스트**: `http://localhost:8080/pdf`
* **이미지 검색 및 분석 테스트**: `http://localhost:8080/image`

<!-- pdf-til-supplement:start -->
## TIL 부연 설명 — PDF와 연결하기

기존 실습 내용을 이해하기 위한 PDF 기반 부연 설명이다. 아래 예시는 개념을 설명하기 위한 것이며, 이 프로젝트에서 실행해 관찰한 결과와는 구분한다. 페이지 번호는 표지를 포함한 PDF 순서다.

함께 읽을 파일: [src/main/java/org/example/aifile/AifileApplication.java](<src/main/java/org/example/aifile/AifileApplication.java>) · [src/main/java/org/example/aifile/controller/ImageController.java](<src/main/java/org/example/aifile/controller/ImageController.java>) · [src/main/java/org/example/aifile/controller/PDFController.java](<src/main/java/org/example/aifile/controller/PDFController.java>)

**현재 실습과 연결:** PDFService.uploadPDF는 PagePdfDocumentReader → TokenTextSplitter → filename 메타데이터 추가 → vectorStore.add 순서로 적재하고 청크 수를 반환한다. ragChat은 topK(3), similarityThreshold(0.5)로 검색한다. 이 메서드의 SearchRequest에는 소유자 필터가 없으므로 아래 권한별 검색 제한은 추가 설계할 개념이다.

### PDF 추출부터 근거 답변까지

PDF RAG는 업로드한 파일에서 텍스트를 추출하고 조각으로 나눈 다음 임베딩해 저장한다. 스캔 PDF에는 텍스트 층이 없어 별도 OCR이 필요할 수 있다. 추출이 비었는데 저장 요청만 성공했다고 지식 적재가 완료된 것으로 판단하면 안 된다.

**예시로 이해하기:** 이상한 답변을 조사할 때 추출 텍스트 → 청크 → 검색 결과 → 최종 프롬프트 순서로 확인한다. 페이지와 문서 ID를 메타데이터에 보존하면 답변의 근거를 원문으로 돌아가 확인할 수 있다. 문서 속 명령문은 앱의 지시가 아니라 검색 자료로 취급한다.

근거: 405-1 PDF 문서로 RAG 구현하기 — [10쪽](<../260629_ex/새 폴더/8-10/405-1_PDF_문서로_RAG_구현하기.pdf#page=10>) · [27쪽](<../260629_ex/새 폴더/8-10/405-1_PDF_문서로_RAG_구현하기.pdf#page=27>) · [32쪽](<../260629_ex/새 폴더/8-10/405-1_PDF_문서로_RAG_구현하기.pdf#page=32>) · [38쪽](<../260629_ex/새 폴더/8-10/405-1_PDF_문서로_RAG_구현하기.pdf#page=38>) · [40쪽](<../260629_ex/새 폴더/8-10/405-1_PDF_문서로_RAG_구현하기.pdf#page=40>) · [43쪽](<../260629_ex/새 폴더/8-10/405-1_PDF_문서로_RAG_구현하기.pdf#page=43>)

### 이미지 해석과 이미지 검색을 구분하기

이미지를 설명하는 캡션을 텍스트로 검색하는 방식과 이미지 자체의 임베딩을 검색하는 방식은 다른 경로다. OCR·모델 해석은 원본의 내용을 놓치거나 잘못 읽을 수 있으므로 원본과 해석 결과를 함께 연결한다. 텍스트·이미지 벡터를 섞으려면 모델의 임베딩 공간 호환성이 전제다.

**예시로 이해하기:** 영수증 이미지에서 합계를 잘못 읽으면 검색 결과가 가까워도 답이 틀릴 수 있다. 추출 수치와 원본을 비교할 수 있도록 이미지 ID를 남긴다. Base64는 바이너리를 문자열로 표현할 뿐 암호화가 아니며 전송 크기도 늘어난다.

근거: 405-2 이미지로 확장하는 멀티모달 RAG — [7쪽](<../260629_ex/새 폴더/8-10/405-2_이미지로_확장하는_멀티모달_RAG.pdf#page=7>) · [10쪽](<../260629_ex/새 폴더/8-10/405-2_이미지로_확장하는_멀티모달_RAG.pdf#page=10>) · [24쪽](<../260629_ex/새 폴더/8-10/405-2_이미지로_확장하는_멀티모달_RAG.pdf#page=24>) · [25쪽](<../260629_ex/새 폴더/8-10/405-2_이미지로_확장하는_멀티모달_RAG.pdf#page=25>) · [37쪽](<../260629_ex/새 폴더/8-10/405-2_이미지로_확장하는_멀티모달_RAG.pdf#page=37>)

### 객체 키와 접근 URL을 나누어 보관하기

객체 스토리지는 버킷 안의 키로 파일을 찾는다. DB에 만료되는 서명 URL보다 안정적인 객체 키를 보관하면 제공 방식과 저장 위치를 바꾸기 쉽다. FileStore 같은 인터페이스는 로컬 저장과 객체 스토리지 구현의 차이를 서비스 밖으로 분리한다.

**예시로 이해하기:** 비공개 파일은 로그인 사용자와 파일 소유 관계를 확인한 뒤 읽기 경로를 제공한다. 스토리지 업로드와 DB 트랜잭션은 별개라 DB가 롤백되어도 업로드한 객체는 남을 수 있다. 보상 삭제나 정리 작업이 필요한 실패 구간을 표시한다.

근거: 404-2 객체 스토리지로 파일 저장하기 — [5쪽](<../260629_ex/새 폴더/8-7/404-2_객체_스토리지로_파일_저장하기.pdf#page=5>) · [27쪽](<../260629_ex/새 폴더/8-7/404-2_객체_스토리지로_파일_저장하기.pdf#page=27>) · [28쪽](<../260629_ex/새 폴더/8-7/404-2_객체_스토리지로_파일_저장하기.pdf#page=28>) · [35쪽](<../260629_ex/새 폴더/8-7/404-2_객체_스토리지로_파일_저장하기.pdf#page=35>) · [41쪽](<../260629_ex/새 폴더/8-7/404-2_객체_스토리지로_파일_저장하기.pdf#page=41>) · [46쪽](<../260629_ex/새 폴더/8-7/404-2_객체_스토리지로_파일_저장하기.pdf#page=46>)

<!-- pdf-til-supplement:end -->

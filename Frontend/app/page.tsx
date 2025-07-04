"use client";

import { useState, useEffect, useRef } from "react";
import Image from "next/image";
import Link from "next/link";
import { Search, ChevronDown } from "lucide-react";
import axios from "axios";

interface UserData {
  user_seq_no: string;
  user_name: string;
  gender: string;
  phone_number: string;
  user_email: string;
  address: string;
  created_at: string;
}

export default function HyundaiCapitalClone() {
  const [hoveredSection, setHoveredSection] = useState<string | null>(null);
  const [userData, setUserData] = useState<UserData | null>(null);
  const [showUserMenu, setShowUserMenu] = useState(false);
  const [showProductMenu, setShowProductMenu] = useState(false);
  const menuRef = useRef<HTMLDivElement>(null);
  const [isHeaderHovered, setIsHeaderHovered] = useState(false);

  useEffect(() => {
    const fetchUserData = async () => {
      const accessToken = sessionStorage.getItem("accessToken");
      const savedUserData = sessionStorage.getItem("userData");

      if (savedUserData) {
        try {
          const userData = JSON.parse(savedUserData);
          console.log("Using saved user data:", userData);
          setUserData(userData);
        } catch (error) {
          console.error("Failed to parse saved user data:", error);
          sessionStorage.removeItem("userData");
        }
      } else if (accessToken) {
        try {
          const response = await axios.get(
            "https://aef2-112-76-112-180.ngrok-free.app/api/users/me",
            {
              headers: {
                Authorization: `Bearer ${accessToken}`,
                "Content-Type": "application/json",
                Accept: "application/json",
                "ngrok-skip-browser-warning": "true",
              },
            }
          );

          console.log("User data loaded:", response.data);
          setUserData(response.data);
          sessionStorage.setItem("userData", JSON.stringify(response.data));
        } catch (error) {
          console.error("Failed to fetch user data:", error);
          sessionStorage.removeItem("accessToken");
          sessionStorage.removeItem("userSeqNo");
          sessionStorage.removeItem("userData");
          setUserData(null);
        }
      }
    };

    fetchUserData();

    // 외부 클릭 시 메뉴 닫기
    const handleClickOutside = (event: MouseEvent) => {
      if (menuRef.current && !menuRef.current.contains(event.target as Node)) {
        setShowUserMenu(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);

  const handleTextClick = (buttonType: string, section: string) => {
    console.log(`${section} - ${buttonType} 텍스트 클릭됨`);
    // 여기에 실제 기능 구현
  };

  const handleLogout = () => {
    sessionStorage.removeItem("accessToken");
    sessionStorage.removeItem("userSeqNo");
    sessionStorage.removeItem("userData");
    setUserData(null);
    setShowUserMenu(false);
  };

  const toggleUserMenu = () => {
    setShowUserMenu(!showUserMenu);
  };

  const toggleProductMenu = () => {
    setShowProductMenu(!showProductMenu);
  };

  return (
    <div className="min-h-screen">
      {/* Main Blue Section - includes header and main content */}
      <section className="main-section">
        {/* Header */}
        <header
          className={`header-nav relative transition-all duration-300 ${
            isHeaderHovered ? "bg-white" : ""
          }`}
          onMouseEnter={() => setIsHeaderHovered(true)}
          onMouseLeave={() => setIsHeaderHovered(false)}
        >
          <div className="max-w-7xl mx-auto px-4 py-3">
            <div className="flex items-center justify-between">
              {/* Logo */}
              <div className="flex items-center space-x-8">
                <Link href="/" className="flex items-center">
                  <Image
                    src={
                      isHeaderHovered
                        ? "/images/hyundai-capital-logo-blue.png"
                        : "/images/hyundai-capital-logo-white.png"
                    }
                    alt="Hyundai Capital"
                    width={120}
                    height={24}
                    className="h-6 w-auto transition-all duration-300"
                  />
                </Link>

                {/* Main Navigation */}
                <nav className="hidden md:flex items-center space-x-8">
                  <Link
                    href="#"
                    className={`nav-link transition-colors duration-300 hover:text-blue-700 ${
                      isHeaderHovered ? "!text-blue-500 font-semibold" : ""
                    }`}
                  >
                    자동차
                  </Link>
                  <Link
                    href="#"
                    className={`nav-link transition-colors duration-300 hover:text-blue-700 ${
                      isHeaderHovered ? "!text-blue-500 font-semibold" : ""
                    }`}
                  >
                    대출
                  </Link>
                  <Link
                    href="#"
                    className={`nav-link transition-colors duration-300 hover:text-blue-700 ${
                      isHeaderHovered ? "!text-blue-500 font-semibold" : ""
                    }`}
                  >
                    H-Coin
                  </Link>
                  <Link
                    href="#"
                    className={`nav-link transition-colors duration-300 hover:text-blue-700 ${
                      isHeaderHovered ? "!text-blue-500 font-semibold" : ""
                    }`}
                  >
                    법인
                  </Link>
                </nav>
              </div>

              {/* Right Navigation */}
              <nav className="hidden lg:flex items-center space-x-6 text-sm">
                {/* 가입 상품 드롭다운 */}
                <button
                  onClick={toggleProductMenu}
                  className={`nav-link-right hover:text-blue-700 flex items-center transition-colors duration-300 ${
                    showProductMenu
                      ? "text-white border-b-2 border-white pb-1"
                      : ""
                  } ${isHeaderHovered ? "!text-blue-500 font-semibold" : ""}`}
                >
                  가입 상품
                </button>

                <Link
                  href="#"
                  className={`nav-link-right transition-colors duration-300 hover:text-blue-700 ${
                    isHeaderHovered ? "!text-blue-500 font-semibold" : ""
                  }`}
                >
                  서비스 이벤트
                </Link>
                <Link
                  href="#"
                  className={`nav-link-right transition-colors duration-300 hover:text-blue-700 ${
                    isHeaderHovered ? "!text-blue-500 font-semibold" : ""
                  }`}
                >
                  고객센터
                </Link>
                <Link
                  href="#"
                  className={`nav-link-right transition-colors duration-300 hover:text-blue-700 ${
                    isHeaderHovered ? "!text-blue-500 font-semibold" : ""
                  }`}
                >
                  금융소비자 보호
                </Link>
                <Link
                  href="#"
                  className={`nav-link-right flex items-center transition-colors duration-300 hover:text-blue-700 ${
                    isHeaderHovered ? "!text-blue-500 font-semibold" : ""
                  }`}
                >
                  회사소개 <span className="ml-1">↗</span>
                </Link>

                {/* User Menu or Login */}
                {userData ? (
                  <div className="relative" ref={menuRef}>
                    <button
                      onClick={toggleUserMenu}
                      className={`nav-link-right flex items-center space-x-1 hover:text-blue-700 transition-colors duration-300 ${
                        isHeaderHovered ? "!text-blue-500 font-semibold" : ""
                      }`}
                    >
                      <span>{userData.user_name}님</span>
                      <ChevronDown className="w-3 h-3" />
                    </button>

                    {/* User Dropdown Menu */}
                    {showUserMenu && (
                      <div className="absolute right-0 top-full mt-2 w-40 bg-white shadow-lg border border-gray-200 z-50">
                        {/* User Name Only */}
                        <div className="px-3 py-2 border-b border-gray-200">
                          <p className="font-medium text-gray-900 text-sm">
                            {userData.user_name}님
                          </p>
                        </div>

                        {/* Menu Buttons - Side by Side */}
                        <div className="flex">
                          <Link
                            href="/loan-history"
                            className="flex-1 px-2 py-2 text-xs text-gray-700 hover:bg-gray-50 transition-colors border-r border-gray-200"
                          >
                            내대출
                          </Link>
                          <button
                            onClick={handleLogout}
                            className="flex-1 px-2 py-2 text-xs text-gray-700 hover:bg-gray-50 transition-colors"
                          >
                            로그아웃
                          </button>
                        </div>
                      </div>
                    )}
                  </div>
                ) : (
                  <Link
                    href="/login"
                    className={`nav-link-right transition-colors duration-300 hover:text-blue-700 ${
                      isHeaderHovered ? "!text-blue-500 font-semibold" : ""
                    }`}
                  >
                    로그인
                  </Link>
                )}

                <Search
                  className={`w-4 h-4 cursor-pointer hover:text-blue-700 transition-colors duration-300 ${
                    isHeaderHovered ? "!text-blue-500" : "text-white/80"
                  }`}
                />
              </nav>
            </div>
          </div>

          {/* Compact Product Dropdown Menu */}
          {showProductMenu && (
            <div className="absolute left-0 right-0 top-full bg-white shadow-lg border-t border-gray-200 z-40">
              <div className="flex justify-center py-8">
                <div className="grid grid-cols-4 gap-16 max-w-4xl">
                  {/* Column 1: 상품 이용내역 */}
                  <div>
                    <h3 className="font-bold text-gray-900 mb-4 text-base">
                      상품 이용내역
                    </h3>
                    <ul className="space-y-2">
                      <li>
                        <Link
                          href="#"
                          className="text-sm text-gray-600 hover:text-blue-600 transition-colors block"
                        >
                          이용명세서
                        </Link>
                      </li>
                      <li>
                        <Link
                          href="/loan-history"
                          className="text-sm text-gray-600 hover:text-blue-600 transition-colors block"
                        >
                          상품 이용내역
                        </Link>
                      </li>
                      <li>
                        <Link
                          href="#"
                          className="text-sm text-gray-600 hover:text-blue-600 transition-colors block"
                        >
                          부채내역
                        </Link>
                      </li>
                      <li>
                        <Link
                          href="#"
                          className="text-sm text-gray-600 hover:text-blue-600 transition-colors block"
                        >
                          미납내역
                        </Link>
                      </li>
                      <li>
                        <Link
                          href="#"
                          className="text-sm text-gray-600 hover:text-blue-600 transition-colors block"
                        >
                          법무서비스 이용현황
                        </Link>
                      </li>
                    </ul>
                  </div>

                  {/* Column 2: 이용금액 결제 */}
                  <div>
                    <h3 className="font-bold text-gray-900 mb-4 text-base">
                      이용금액 결제
                    </h3>
                    <ul className="space-y-2">
                      <li>
                        <Link
                          href="#"
                          className="text-sm text-gray-600 hover:text-blue-600 transition-colors block"
                        >
                          청구금액 결제
                        </Link>
                      </li>
                      <li>
                        <Link
                          href="#"
                          className="text-sm text-gray-600 hover:text-blue-600 transition-colors block"
                        >
                          H-Coin 청구금액 신청
                        </Link>
                      </li>
                      <li>
                        <Link
                          href="#"
                          className="text-sm text-gray-600 hover:text-blue-600 transition-colors block"
                        >
                          충돌상황
                        </Link>
                      </li>
                      <li>
                        <Link
                          href="#"
                          className="text-sm text-gray-600 hover:text-blue-600 transition-colors block"
                        >
                          예수금 사용
                        </Link>
                      </li>
                    </ul>
                  </div>

                  {/* Column 3: 결제정보 관리 */}
                  <div>
                    <h3 className="font-bold text-gray-900 mb-4 text-base">
                      결제정보 관리
                    </h3>
                    <ul className="space-y-2">
                      <li>
                        <Link
                          href="#"
                          className="text-sm text-gray-600 hover:text-blue-600 transition-colors block"
                        >
                          결제정보 조회 변경
                        </Link>
                      </li>
                    </ul>
                  </div>

                  {/* Column 4: 회원정보 관리 */}
                  <div>
                    <h3 className="font-bold text-gray-900 mb-4 text-base">
                      회원정보 관리
                    </h3>
                    <ul className="space-y-2">
                      <li>
                        <Link
                          href="#"
                          className="text-sm text-gray-600 hover:text-blue-600 transition-colors block"
                        >
                          회원정보 조회 변경
                        </Link>
                      </li>
                    </ul>
                  </div>
                </div>
              </div>
            </div>
          )}
        </header>

        {/* Main Content */}
        <div className="max-w-7xl mx-auto pt-16">
          <div className="grid md:grid-cols-3 h-full min-h-[350px]">
            {/* 자동차금융 */}
            <div
              className={`loan-section ${
                hoveredSection && hoveredSection !== "auto"
                  ? "dimmed"
                  : "active"
              }`}
              onMouseEnter={() => setHoveredSection("auto")}
              onMouseLeave={() => setHoveredSection(null)}
            >
              <div>
                <h2 className="loan-title">자동차금융.</h2>
                <p className="loan-description">어떤 차량을 원하세요?</p>
              </div>
              <div className="loan-buttons">
                <div className="loan-button">
                  <span
                    className="button-text"
                    onClick={() => handleTextClick("신차", "자동차금융")}
                  >
                    신차
                  </span>
                  <span
                    className="button-text"
                    onClick={() => handleTextClick("중고차", "자동차금융")}
                  >
                    중고차
                  </span>
                </div>
              </div>
            </div>

            {/* 신용대출 */}
            <div
              className={`loan-section ${
                hoveredSection && hoveredSection !== "credit"
                  ? "dimmed"
                  : "active"
              }`}
              onMouseEnter={() => setHoveredSection("credit")}
              onMouseLeave={() => setHoveredSection(null)}
            >
              <div>
                <h2 className="loan-title">신용대출.</h2>
                <p className="loan-description">어떤 조건을 더 선호하세요?</p>
              </div>
              <div className="loan-buttons">
                <div className="loan-button">
                  <span
                    className="button-text"
                    onClick={() => handleTextClick("신용대출", "신용대출")}
                  >
                    신용대출
                  </span>
                  <span
                    className="button-text"
                    onClick={() =>
                      handleTextClick("자동차담보대출", "신용대출")
                    }
                  >
                    자동차담보대출
                  </span>
                </div>
              </div>
            </div>

            {/* 주택대출 */}
            <div
              className={`loan-section ${
                hoveredSection && hoveredSection !== "home"
                  ? "dimmed"
                  : "active"
              }`}
              onMouseEnter={() => setHoveredSection("home")}
              onMouseLeave={() => setHoveredSection(null)}
            >
              <div>
                <h2 className="loan-title">주택대출.</h2>
                <p className="loan-description">어떤 대출이 필요하신가요?</p>
              </div>
              <div className="loan-buttons">
                <div className="loan-button">
                  <span
                    className="button-text"
                    onClick={() => handleTextClick("주택담보대출", "주택대출")}
                  >
                    주택담보대출
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Bottom Services Section */}
      <section className="bg-gray-100 py-12">
        <div className="max-w-6xl mx-auto px-4">
          <div className="grid md:grid-cols-2 lg:grid-cols-4 gap-4">
            {/* 할부 - 1.jpg (팰리세이드) */}
            <div className="service-card group">
              <div className="service-card-image-container">
                <Image
                  src="/images/car1.jpg"
                  alt="할부 서비스"
                  width={400}
                  height={200}
                  className="service-card-image"
                />
                <div className="service-card-title-overlay">
                  <h3>할부</h3>
                </div>
              </div>
              <div className="service-card-content">
                <p className="service-card-description">
                  현대자동차·기아 구매 시
                  <br />
                  특별 프로모션을 확인하세요!
                </p>
              </div>
            </div>

            {/* 내 차 팔기 - 2.jpg (제네시스 쿠페) */}
            <div className="service-card group">
              <div className="service-card-image-container">
                <Image
                  src="/images/car2.jpg"
                  alt="내 차 팔기 서비스"
                  width={400}
                  height={200}
                  className="service-card-image"
                />
                <div className="service-card-title-overlay">
                  <h3>내 차 팔기</h3>
                </div>
              </div>
              <div className="service-card-content">
                <p className="service-card-description">
                  방문부터 매각까지 전 과정 수수료 0원!
                </p>
              </div>
            </div>

            {/* 신차할부 승계 서비스 - 3.jpg (대형 SUV) */}
            <div className="service-card group">
              <div className="service-card-image-container">
                <Image
                  src="/images/car3.jpg"
                  alt="신차할부 승계 서비스"
                  width={400}
                  height={200}
                  className="service-card-image"
                />
                <div className="service-card-title-overlay">
                  <h3>신차할부 승계 서비스</h3>
                </div>
              </div>
              <div className="service-card-content">
                <p className="service-card-description">
                  온라인으로 간편하게 승계 신청하세요!
                </p>
              </div>
            </div>

            {/* 리스·렌트 승계 서비스 - 4.jpg (노란색 SUV) */}
            <div className="service-card group">
              <div className="service-card-image-container">
                <Image
                  src="/images/car4.jpg"
                  alt="리스·렌트 승계 서비스"
                  width={400}
                  height={200}
                  className="service-card-image"
                />
                <div className="service-card-title-overlay">
                  <h3>리스·렌트 승계 서비스</h3>
                </div>
              </div>
              <div className="service-card-content">
                <p className="service-card-description">
                  이제 리스·렌트 승계도
                  <br />
                  온라인으로 신청하세요!
                </p>
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}

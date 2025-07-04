"use client";

import { useState, useEffect, useRef } from "react";
import Image from "next/image";
import Link from "next/link";
import { Search, ChevronDown, ChevronUp } from "lucide-react";
import axios from "axios";

interface UserData {
  isLoggedIn: boolean;
  userName: string;
  userEmail: string;
  userId: string;
}

interface LoanData {
  loan_id: string;
  loan_account_num: string;
  loan_product_name: string;
  loan_amount: number;
  remaining_amount: number;
  interest_rate: number;
  contract_date: string;
  maturity_date: string;
  repayment_day: number;
  loan_status: string;
  loan_type: string;
  user_seq_no: string;
  user_name: string;
  repayment_info: {
    repay_date: string;
    repay_method: string;
    repay_org_code: string;
    repay_account_num: string;
    repay_account_num_masked: string;
    next_repay_date: string;
  };
  recent_transactions: Array<{
    transaction_id: string;
    transaction_date: string;
    transaction_time: string;
    transaction_type: string;
    transaction_amount: number;
    after_balance: number;
    transaction_summary: string;
  }>;
}

export default function LoanHistoryPage() {
  const [userData, setUserData] = useState<UserData | null>(null);
  const [showUserMenu, setShowUserMenu] = useState(false);
  const [showProductMenu, setShowProductMenu] = useState(false);
  const menuRef = useRef<HTMLDivElement>(null);
  const [isHeaderHovered, setIsHeaderHovered] = useState(false);
  const [expandedLoans, setExpandedLoans] = useState<Set<string>>(new Set());
  const [loanDataList, setLoanDataList] = useState<LoanData[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    // 로그인 상태 확인
    const savedUserData = localStorage.getItem("userData");
    if (savedUserData) {
      setUserData(JSON.parse(savedUserData));
    }

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

  // 대출 데이터 가져오기
  useEffect(() => {
    const fetchLoans = async () => {
      try {
        const userSeqNo = sessionStorage.getItem("userSeqNo");
        const accessToken = sessionStorage.getItem("accessToken");

        console.log("Fetching loans with userSeqNo:", userSeqNo);
        console.log("Access Token:", accessToken);

        if (!userSeqNo || !accessToken) {
          console.log("Missing userSeqNo or accessToken");
          setError("로그인이 필요합니다");
          setLoading(false);
          return;
        }

        const response = await axios({
          method: "get",
          url: `https://aef2-112-76-112-180.ngrok-free.app/api/hyundai-capital/loans/my-loans?user_seq_no=${userSeqNo}`,
          headers: {
            Authorization: `Bearer ${accessToken}`,
            "Content-Type": "application/json",
            Accept: "application/json",
            "ngrok-skip-browser-warning": "true",
          },
        });

        console.log("API Response:", response.data);

        if (Array.isArray(response.data)) {
          setLoanDataList(response.data);
          setError(null);
        } else {
          throw new Error("응답 데이터 형식이 올바르지 않습니다");
        }
      } catch (error: any) {
        console.error("API Error:", error);
        setError(
          error.response?.data?.message || "대출 정보를 불러오는데 실패했습니다"
        );
      } finally {
        setLoading(false);
      }
    };

    fetchLoans();
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("userData");
    sessionStorage.removeItem("accessToken");
    sessionStorage.removeItem("userSeqNo");
    setUserData(null);
    setShowUserMenu(false);
  };

  const toggleUserMenu = () => {
    setShowUserMenu(!showUserMenu);
  };

  const toggleProductMenu = () => {
    setShowProductMenu(!showProductMenu);
  };

  const toggleLoanDetails = (loanId: string) => {
    const newExpanded = new Set(expandedLoans);
    if (newExpanded.has(loanId)) {
      newExpanded.delete(loanId);
    } else {
      newExpanded.add(loanId);
    }
    setExpandedLoans(newExpanded);
  };

  const formatDate = (dateStr: string) => {
    const year = dateStr.substring(0, 4);
    const month = dateStr.substring(4, 6);
    const day = dateStr.substring(6, 8);
    return `${year}.${month}.${day}`;
  };

  const formatAmount = (amount: number) => {
    return amount.toLocaleString() + "원";
  };

  const getStatusText = (status: string) => {
    switch (status) {
      case "ACTIVE":
        return "정상";
      default:
        return status;
    }
  };

  const getTransactionTypeText = (type: string) => {
    switch (type) {
      case "LOAN_EXECUTION":
        return "대출실행";
      case "TOTAL_PAYMENT":
        return "상환";
      default:
        return type;
    }
  };

  return (
    <div className="min-h-screen bg-white">
      {/* Header - 메인 페이지와 동일 */}
      <header
        className={`bg-white shadow-sm relative transition-all duration-300 ${
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
                  src="/images/hyundai-capital-logo-blue.png"
                  alt="Hyundai Capital"
                  width={120}
                  height={24}
                  className="h-6 w-auto"
                />
              </Link>

              {/* Main Navigation */}
              <nav className="hidden md:flex items-center space-x-8">
                <Link
                  href="#"
                  className="text-gray-700 hover:text-blue-600 font-medium transition-colors"
                >
                  자동차
                </Link>
                <Link
                  href="#"
                  className="text-gray-700 hover:text-blue-600 font-medium transition-colors"
                >
                  대출
                </Link>
                <Link
                  href="#"
                  className="text-gray-700 hover:text-blue-600 font-medium transition-colors"
                >
                  H-Coin
                </Link>
                <Link
                  href="#"
                  className="text-gray-700 hover:text-blue-600 font-medium transition-colors"
                >
                  법인
                </Link>
              </nav>
            </div>

            {/* Right Navigation */}
            <nav className="hidden lg:flex items-center space-x-6 text-sm">
              <button
                onClick={toggleProductMenu}
                className="text-gray-600 hover:text-blue-600 flex items-center transition-colors"
              >
                가입 상품
              </button>
              <Link
                href="#"
                className="text-gray-600 hover:text-blue-600 transition-colors"
              >
                서비스 이벤트
              </Link>
              <Link
                href="#"
                className="text-gray-600 hover:text-blue-600 transition-colors"
              >
                고객센터
              </Link>
              <Link
                href="#"
                className="text-gray-600 hover:text-blue-600 transition-colors"
              >
                금융소비자 보호
              </Link>
              <Link
                href="#"
                className="text-gray-600 hover:text-blue-600 flex items-center transition-colors"
              >
                회사소개 <span className="ml-1">↗</span>
              </Link>

              {/* User Menu */}
              {userData ? (
                <div className="relative" ref={menuRef}>
                  <button
                    onClick={toggleUserMenu}
                    className="text-gray-600 hover:text-blue-600 flex items-center space-x-1 transition-colors"
                  >
                    <span>{userData.userName}님</span>
                    <ChevronDown className="w-3 h-3" />
                  </button>

                  {showUserMenu && (
                    <div className="absolute right-0 top-full mt-2 w-40 bg-white shadow-lg border border-gray-200 z-50">
                      <div className="px-3 py-2 border-b border-gray-200">
                        <p className="font-medium text-gray-900 text-sm">
                          {userData.userName}님
                        </p>
                      </div>
                      <div className="flex">
                        <button className="flex-1 px-2 py-2 text-xs text-gray-700 hover:bg-gray-50 transition-colors border-r border-gray-200">
                          내대출
                        </button>
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
                  className="text-gray-600 hover:text-blue-600 transition-colors"
                >
                  로그인
                </Link>
              )}

              <Search className="w-4 h-4 text-gray-600 cursor-pointer hover:text-blue-600 transition-colors" />
            </nav>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <div className="max-w-4xl mx-auto px-4 py-8">
        {/* Page Title */}
        <h1 className="text-3xl font-bold text-gray-900 mb-8 text-center">
          상품 이용내역
        </h1>

        {/* Loading State */}
        {loading && (
          <div className="text-center py-8">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600 mx-auto"></div>
            <p className="mt-4 text-gray-600">
              대출 정보를 불러오는 중입니다...
            </p>
          </div>
        )}

        {/* Error State */}
        {error && (
          <div className="text-center py-8 text-red-500">
            <p>{error}</p>
            <p className="text-sm mt-2">잠시 후 다시 시도해주세요.</p>
          </div>
        )}

        {/* Empty State */}
        {!loading && !error && loanDataList.length === 0 && (
          <div className="text-center py-8 text-gray-600">
            대출 내역이 없습니다
          </div>
        )}

        {/* Loan List */}
        {!loading && !error && loanDataList.length > 0 && (
          <div className="space-y-0">
            {loanDataList.map((loanData, index) => (
              <div key={loanData.loan_id}>
                {/* Blue separator line (except for first item) */}
                {index > 0 && <div className="h-px bg-blue-500 my-8"></div>}

                <div className="bg-white">
                  {/* Loan Summary Card */}
                  <div className="p-6">
                    <div className="flex justify-between items-start mb-4">
                      <div>
                        <h2 className="text-xl font-bold text-gray-900 mb-2">
                          {loanData.loan_product_name}
                        </h2>
                        <p className="text-sm text-gray-500">
                          계좌번호: {loanData.loan_account_num}
                        </p>
                      </div>
                      <span
                        className={`px-3 py-1 rounded-full text-sm font-medium ${
                          loanData.loan_status === "ACTIVE"
                            ? "bg-green-100 text-green-800"
                            : "bg-gray-100 text-gray-800"
                        }`}
                      >
                        {getStatusText(loanData.loan_status)}
                      </span>
                    </div>

                    <div className="grid md:grid-cols-4 gap-6 mb-4">
                      <div>
                        <p className="text-sm text-gray-500 mb-1">대출금액</p>
                        <p className="text-lg font-semibold text-gray-900">
                          {formatAmount(loanData.loan_amount)}
                        </p>
                      </div>
                      <div>
                        <p className="text-sm text-gray-500 mb-1">잔여금액</p>
                        <p className="text-lg font-semibold text-blue-600">
                          {formatAmount(loanData.remaining_amount)}
                        </p>
                      </div>
                      <div>
                        <p className="text-sm text-gray-500 mb-1">금리</p>
                        <p className="text-lg font-semibold text-gray-900">
                          {loanData.interest_rate}%
                        </p>
                      </div>
                      <div>
                        <p className="text-sm text-gray-500 mb-1">만기일</p>
                        <p className="text-lg font-semibold text-gray-900">
                          {formatDate(loanData.maturity_date)}
                        </p>
                      </div>
                    </div>

                    {/* Toggle Button */}
                    <div className="flex justify-center">
                      <button
                        onClick={() => toggleLoanDetails(loanData.loan_id)}
                        className="flex items-center space-x-2 px-4 py-2 text-blue-600 hover:text-blue-700 transition-colors"
                      >
                        <span>
                          {expandedLoans.has(loanData.loan_id)
                            ? "간단히 보기"
                            : "상세보기"}
                        </span>
                        {expandedLoans.has(loanData.loan_id) ? (
                          <ChevronUp className="w-4 h-4" />
                        ) : (
                          <ChevronDown className="w-4 h-4" />
                        )}
                      </button>
                    </div>
                  </div>

                  {/* Expanded Details */}
                  {expandedLoans.has(loanData.loan_id) && (
                    <div className="border-t border-gray-200">
                      {/* Repayment Info */}
                      <div className="p-6 border-b border-gray-200">
                        <h3 className="text-lg font-bold text-gray-900 mb-4">
                          상환 정보
                        </h3>
                        <div className="grid md:grid-cols-3 gap-6">
                          <div>
                            <p className="text-sm text-gray-500 mb-1">상환일</p>
                            <p className="text-base font-medium text-gray-900">
                              매월 {loanData.repayment_day}일
                            </p>
                          </div>
                          <div>
                            <p className="text-sm text-gray-500 mb-1">
                              다음 상환일
                            </p>
                            <p className="text-base font-medium text-gray-900">
                              {formatDate(
                                loanData.repayment_info.next_repay_date
                              )}
                            </p>
                          </div>
                          <div>
                            <p className="text-sm text-gray-500 mb-1">
                              상환계좌
                            </p>
                            <p className="text-base font-medium text-gray-900">
                              {loanData.repayment_info.repay_account_num_masked}
                            </p>
                          </div>
                        </div>
                      </div>

                      {/* Transaction History */}
                      <div className="p-6">
                        <h3 className="text-lg font-bold text-gray-900 mb-4">
                          최근 거래내역
                        </h3>
                        <div className="overflow-x-auto">
                          <table className="w-full">
                            <thead>
                              <tr className="border-b border-gray-200">
                                <th className="text-left py-3 px-4 font-medium text-gray-700">
                                  거래일시
                                </th>
                                <th className="text-left py-3 px-4 font-medium text-gray-700">
                                  거래구분
                                </th>
                                <th className="text-right py-3 px-4 font-medium text-gray-700">
                                  거래금액
                                </th>
                                <th className="text-right py-3 px-4 font-medium text-gray-700">
                                  잔액
                                </th>
                                <th className="text-left py-3 px-4 font-medium text-gray-700">
                                  거래내용
                                </th>
                              </tr>
                            </thead>
                            <tbody>
                              {loanData.recent_transactions.map(
                                (transaction) => (
                                  <tr
                                    key={transaction.transaction_id}
                                    className="border-b border-gray-100 hover:bg-gray-50"
                                  >
                                    <td className="py-3 px-4 text-sm text-gray-900">
                                      {formatDate(transaction.transaction_date)}
                                    </td>
                                    <td className="py-3 px-4 text-sm text-gray-900">
                                      {getTransactionTypeText(
                                        transaction.transaction_type
                                      )}
                                    </td>
                                    <td className="py-3 px-4 text-sm text-right font-medium">
                                      <span
                                        className={
                                          transaction.transaction_type ===
                                          "LOAN_EXECUTION"
                                            ? "text-blue-600"
                                            : "text-red-600"
                                        }
                                      >
                                        {transaction.transaction_type ===
                                        "LOAN_EXECUTION"
                                          ? "+"
                                          : "-"}
                                        {formatAmount(
                                          transaction.transaction_amount
                                        )}
                                      </span>
                                    </td>
                                    <td className="py-3 px-4 text-sm text-right text-gray-900">
                                      {formatAmount(transaction.after_balance)}
                                    </td>
                                    <td className="py-3 px-4 text-sm text-gray-600">
                                      {transaction.transaction_summary}
                                    </td>
                                  </tr>
                                )
                              )}
                            </tbody>
                          </table>
                        </div>
                      </div>
                    </div>
                  )}
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

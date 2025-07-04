"use client"

import type React from "react"

import { useState } from "react"
import Image from "next/image"
import Link from "next/link"
import { Search } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { useRouter } from "next/navigation"

export default function LoginPage() {
  const [activeTab, setActiveTab] = useState("아이디")
  const [formData, setFormData] = useState({
    username: "",
    password: "",
  })

  const router = useRouter()

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }))
  }

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    console.log("로그인 시도:", formData)

    // 로그인 성공 시 사용자 정보 저장
    const userData = {
      isLoggedIn: true,
      userName: "김현대", // 임시 사용자 이름
      userEmail: "kim.hyundai@example.com",
      userId: formData.username || "user123",
    }

    localStorage.setItem("userData", JSON.stringify(userData))

    // 홈으로 리다이렉트
    router.push("/")
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-400 via-blue-500 to-blue-600">
      {/* Header - 메인 페이지와 동일 */}
      <header className="header-nav">
        <div className="max-w-7xl mx-auto px-4 py-3">
          <div className="flex items-center justify-between">
            {/* Logo */}
            <div className="flex items-center space-x-8">
              <Link href="/" className="flex items-center">
                <Image
                  src="/images/hyundai-capital-logo-white.png"
                  alt="Hyundai Capital"
                  width={120}
                  height={24}
                  className="h-6 w-auto"
                />
              </Link>

              {/* Main Navigation */}
              <nav className="hidden md:flex items-center space-x-8">
                <Link href="#" className="nav-link">
                  자동차
                </Link>
                <Link href="#" className="nav-link">
                  대출
                </Link>
                <Link href="#" className="nav-link">
                  H-Coin
                </Link>
                <Link href="#" className="nav-link">
                  법인
                </Link>
              </nav>
            </div>

            {/* Right Navigation */}
            <nav className="hidden lg:flex items-center space-x-6 text-sm">
              <Link href="#" className="nav-link-right">
                가입 상품
              </Link>
              <Link href="#" className="nav-link-right">
                서비스 이벤트
              </Link>
              <Link href="#" className="nav-link-right">
                고객센터
              </Link>
              <Link href="#" className="nav-link-right">
                금융소비자 보호
              </Link>
              <Link href="#" className="nav-link-right flex items-center">
                회사소개 <span className="ml-1">↗</span>
              </Link>
              <Link href="#" className="nav-link-right">
                로그인
              </Link>
              <Search className="w-4 h-4 text-white/80 cursor-pointer hover:text-white" />
            </nav>
          </div>
        </div>
      </header>

      {/* Main Login Content */}
      <div className="flex flex-col items-center justify-center px-4 pt-20">
        {/* Tab Navigation */}
        <div className="flex mb-12 space-x-12">
          {["아이디", "인증서", "사업자번호"].map((tab) => (
            <button
              key={tab}
              onClick={() => setActiveTab(tab)}
              className={`text-lg font-medium transition-all pb-2 ${
                activeTab === tab ? "text-white border-b-2 border-white" : "text-white/70 hover:text-white"
              }`}
            >
              {tab}
            </button>
          ))}
        </div>

        {/* Login Form */}
        <div className="w-full max-w-md">
          <form onSubmit={handleSubmit} className="space-y-4">
            {/* Login Form Container */}
            <div className="bg-white rounded-lg overflow-hidden">
              {/* Username Input - 상단 절반 */}
              <div className="h-16 border-b border-gray-200">
                <Input
                  name="username"
                  type="text"
                  placeholder="아이디 입력"
                  value={formData.username}
                  onChange={handleInputChange}
                  className="w-full h-full px-4 border-0 bg-white rounded-none text-gray-800 placeholder-gray-400 focus:ring-0 focus:outline-none"
                />
              </div>

              {/* Password Input - 하단 절반 */}
              <div className="h-16">
                <Input
                  name="password"
                  type="password"
                  placeholder="비밀번호 입력"
                  value={formData.password}
                  onChange={handleInputChange}
                  className="w-full h-full px-4 border-0 bg-white rounded-none text-gray-800 placeholder-gray-400 focus:ring-0 focus:outline-none"
                />
              </div>
            </div>

            {/* Login Button */}
            <Button
              type="submit"
              className="w-full h-14 bg-white/20 hover:bg-white/30 text-white font-medium rounded-full border border-white/30 backdrop-blur-sm transition-all mt-8"
            >
              로그인
            </Button>
          </form>

          {/* Links */}
          <div className="flex justify-center space-x-6 mt-8 text-white/80 text-sm">
            <Link href="#" className="hover:text-white transition-colors flex items-center">
              아이디 찾기 <span className="ml-1">→</span>
            </Link>
            <Link href="#" className="hover:text-white transition-colors flex items-center">
              비밀번호 찾기 <span className="ml-1">→</span>
            </Link>
            <Link href="#" className="hover:text-white transition-colors flex items-center">
              회원가입 <span className="ml-1">→</span>
            </Link>
          </div>

          {/* App Link */}
          <div className="text-center mt-8">
            <Link
              href="#"
              className="text-white/80 text-sm hover:text-white transition-colors flex items-center justify-center"
            >
              앱에서 가입하셨다면? <span className="ml-1">→</span>
            </Link>
          </div>
        </div>
      </div>
    </div>
  )
}

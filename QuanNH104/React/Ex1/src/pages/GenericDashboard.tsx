import React, { useState } from "react";
import Button from "../components/shared/Button";
import CrudActionBar from "../components/shared/CrudActionBar";
import CrudTable from "../components/shared/CrudTable";
import type { RowAction } from "../components/shared/CrudTable";

// 1. Định nghĩa các interface cho mô hình dữ liệu
interface Branch {
  id: string;
  branchName: string;
  address: string;
}

interface Zone {
  id: string;
  zoneName: string;
  pricePerHour: number;
  branchId: string; // Liên kết với Branch.id
}

interface Computer {
  id: string;
  pcName: string;
  zoneId: string; // Liên kết với Zone.id
  status: "Available" | "Playing" | "Maintenance";
}

interface Food {
  id: string;
  foodName: string;
  price: number;
  category: "Drink" | "Food" | "Combo";
}

// 2. Khởi tạo Mock Data
// 2. Khởi tạo Mock Data quy mô lớn bằng Script
const initialBranches: Branch[] = [
  { id: "B-Q1", branchName: "Cyber Q1 - Nguyễn Du", address: "79 Nguyễn Du, Quận 1, TP.HCM" },
  { id: "B-Q3", branchName: "Cyber Q3 - Cao Thắng", address: "123 Cao Thắng, Quận 3, TP.HCM" },
  { id: "B-Q7", branchName: "Cyber Q7 - Nguyễn Thị Thập", address: "456 Nguyễn Thị Thập, Quận 7, TP.HCM" },
  { id: "B-Q10", branchName: "Cyber Q10 - Sư Vạn Hạnh", address: "789 Sư Vạn Hạnh, Quận 10, TP.HCM" },
  { id: "B-BT", branchName: "Cyber Bình Thạnh - Điện Biên Phủ", address: "555 Điện Biên Phủ, Bình Thạnh, TP.HCM" },
];

const zoneTemplates = [
  { suffix: "STD", name: "Khu Standard", price: 10000 },
  { suffix: "VIP", name: "Khu VIP Gaming", price: 18000 },
  { suffix: "PRE", name: "Khu Premium Glass", price: 25000 },
  { suffix: "FPS", name: "Khu FPS Pro 240Hz", price: 22000 },
  { suffix: "CP", name: "Khu Couple Sofa", price: 30000 },
  { suffix: "STR", name: "Phòng Streamer Solo", price: 35000 },
];

const initialZones: Zone[] = [];
const cyberZoneMapping: { [key: string]: number[] } = {
  "B-Q1": [0, 1, 3, 5],     // Standard, VIP, FPS, Streamer
  "B-Q3": [0, 1, 4, 5],     // Standard, VIP, Couple, Streamer
  "B-Q7": [0, 1, 2, 3],     // Standard, VIP, Premium, FPS
  "B-Q10": [0, 1, 2, 3, 4, 5], // Đầy đủ 6 loại Zone
  "B-BT": [0, 1, 4],        // Standard, VIP, Couple
};

initialBranches.forEach(branch => {
  const templateIndexes = cyberZoneMapping[branch.id] || [0, 1];
  templateIndexes.forEach(idx => {
    const template = zoneTemplates[idx];
    initialZones.push({
      id: `Z-${template.suffix}-${branch.id.replace("B-", "")}`,
      zoneName: `${template.name} (${branch.branchName.split(" - ")[0]})`,
      pricePerHour: template.price,
      branchId: branch.id,
    });
  });
});

const initialComputers: Computer[] = [];
for (let i = 1; i <= 300; i++) {
  const zoneIndex = (i - 1) % initialZones.length;
  const targetZone = initialZones[zoneIndex];
  const cyberCode = targetZone.branchId.replace("B-", "");
  
  // Trạng thái ngẫu nhiên: 70% Available, 20% Playing, 10% Maintenance
  const rand = Math.random();
  let status: "Available" | "Playing" | "Maintenance" = "Available";
  if (rand > 0.9) {
    status = "Maintenance";
  } else if (rand > 0.7) {
    status = "Playing";
  }

  initialComputers.push({
    id: `PC-${String(i).padStart(3, "0")}`,
    pcName: `Máy Số ${String(i).padStart(3, "0")} (${cyberCode})`,
    zoneId: targetZone.id,
    status,
  });
}

const initialFoods: Food[] = [
  { id: "F-01", foodName: "Mì Xào Bò Trứng", price: 35000, category: "Food" },
  { id: "F-02", foodName: "Sting Dâu Lạnh", price: 15000, category: "Drink" },
  { id: "F-03", foodName: "Combo Net Đêm (Mì + Nước)", price: 45000, category: "Combo" },
];

const GenericDashboard: React.FC = () => {
  // Quản lý state CRUD độc lập cho các thực thể
  const [branches, setBranches] = useState<Branch[]>(initialBranches);
  const [zones, setZones] = useState<Zone[]>(initialZones);
  const [computers, setComputers] = useState<Computer[]>(initialComputers);
  const [foods, setFoods] = useState<Food[]>(initialFoods);

  // Điều hướng tabs
  const [activeTab, setActiveTab] = useState<"branches" | "zones" | "computers" | "foods">("branches");

  // State bộ lọc Chi nhánh/Cyber dùng để lọc Máy Trạm
  const [selectedBranchId, setSelectedBranchId] = useState<string>("all");

  // State quản lý Modal Form (Thêm / Sửa)
  const [modalOpen, setModalOpen] = useState(false);
  const [modalMode, setModalMode] = useState<"add" | "edit">("add");
  const [editingItem, setEditingItem] = useState<any>(null);

  // --- Form States ---
  // Branch Form
  const [branchId, setBranchId] = useState("");
  const [branchName, setBranchName] = useState("");
  const [branchAddress, setBranchAddress] = useState("");

  // Zone Form
  const [zoneId, setZoneId] = useState("");
  const [zoneName, setZoneName] = useState("");
  const [zonePrice, setZonePrice] = useState<number>(10000);
  const [zoneBranchId, setZoneBranchId] = useState("");

  // Computer Form
  const [pcId, setPcId] = useState("");
  const [pcName, setPcName] = useState("");
  const [pcZoneId, setPcZoneId] = useState("");
  const [pcStatus, setPcStatus] = useState<"Available" | "Playing" | "Maintenance">("Available");

  // Food Form
  const [foodId, setFoodId] = useState("");
  const [foodName, setFoodName] = useState("");
  const [foodPrice, setFoodPrice] = useState<number>(15000);
  const [foodCategory, setFoodCategory] = useState<"Drink" | "Food" | "Combo">("Food");

  // Toast Notification state
  const [toastMessage, setToastMessage] = useState<string | null>(null);

  const showToast = (message: string) => {
    setToastMessage(message);
    setTimeout(() => {
      setToastMessage(null);
    }, 3000);
  };

  // Tìm ID Chi nhánh (Cyber) của một Máy trạm
  const getBranchIdOfComputer = (pc: Computer) => {
    const zone = zones.find(z => z.id === pc.zoneId);
    return zone ? zone.branchId : "";
  };

  // Lọc danh sách máy trạm theo Cyber được chọn
  const filteredComputers = selectedBranchId === "all"
    ? computers
    : computers.filter(pc => getBranchIdOfComputer(pc) === selectedBranchId);

  // ==================== 1. XỬ LÝ STATE CHO CHI NHÁNH (BRANCHES) ====================
  const handleAddBranchClick = () => {
    setModalMode("add");
    setBranchId("");
    setBranchName("");
    setBranchAddress("");
    setModalOpen(true);
  };

  const handleExportBranches = () => {
    console.log("=== DANH SÁCH CHI NHÁNH (BRANCHES) ===");
    console.log(JSON.stringify(branches, null, 2));
    showToast("Đã xuất danh sách Chi nhánh ra Console log!");
  };

  const handleEditBranchClick = (branch: Branch) => {
    setModalMode("edit");
    setEditingItem(branch);
    setBranchId(branch.id);
    setBranchName(branch.branchName);
    setBranchAddress(branch.address);
    setModalOpen(true);
  };

  const handleDeleteBranchClick = (branch: Branch) => {
    const confirmed = window.confirm(
      `Xóa Chi nhánh "${branch.branchName}" sẽ đồng thời xóa toàn bộ Zone và Máy trạm thuộc chi nhánh này. Bạn vẫn muốn tiếp tục?`
    );
    if (confirmed) {
      setBranches(prev => prev.filter(b => b.id !== branch.id));
      const zonesToDelete = zones.filter(z => z.branchId === branch.id).map(z => z.id);
      setZones(prev => prev.filter(z => z.branchId !== branch.id));
      setComputers(prev => prev.filter(pc => !zonesToDelete.includes(pc.zoneId)));
      showToast(`Đã xóa Chi nhánh "${branch.branchName}" và dữ liệu liên quan!`);
    }
  };

  // ==================== 2. XỬ LÝ STATE CHO KHU VỰC (ZONES) ====================
  const handleAddZoneClick = () => {
    if (branches.length === 0) {
      alert("Vui lòng tạo ít nhất một Chi nhánh trước!");
      return;
    }
    setModalMode("add");
    setZoneId("");
    setZoneName("");
    setZonePrice(10000);
    setZoneBranchId(branches[0].id);
    setModalOpen(true);
  };

  const handleExportZones = () => {
    console.log("=== DANH SÁCH KHU VỰC MÁY (ZONES) ===");
    console.log(JSON.stringify(zones, null, 2));
    showToast("Đã xuất danh sách Zone ra Console log!");
  };

  const handleEditZoneClick = (zone: Zone) => {
    setModalMode("edit");
    setEditingItem(zone);
    setZoneId(zone.id);
    setZoneName(zone.zoneName);
    setZonePrice(zone.pricePerHour);
    setZoneBranchId(zone.branchId);
    setModalOpen(true);
  };

  const handleDeleteZoneClick = (zone: Zone) => {
    const confirmed = window.confirm(
      `Xóa Khu vực "${zone.zoneName}" sẽ đồng thời xóa toàn bộ Máy trạm thuộc khu vực này. Bạn vẫn muốn xóa?`
    );
    if (confirmed) {
      setZones(prev => prev.filter(z => z.id !== zone.id));
      setComputers(prev => prev.filter(pc => pc.zoneId !== zone.id));
      showToast(`Đã xóa Khu vực "${zone.zoneName}" thành công!`);
    }
  };

  // ==================== 3. XỬ LÝ STATE CHO MÁY TRẠM (COMPUTERS) ====================
  const handleAddComputerClick = () => {
    // Chỉ lấy những Zone thuộc Cyber đang chọn (nếu không phải là 'all')
    const activeZones = selectedBranchId === "all"
      ? zones
      : zones.filter(z => z.branchId === selectedBranchId);

    if (activeZones.length === 0) {
      alert(`Vui lòng cấu hình ít nhất một Khu vực máy cho Chi nhánh đang chọn trước khi tạo máy!`);
      return;
    }

    setModalMode("add");
    setPcId("");
    setPcName("");
    setPcZoneId(activeZones[0].id);
    setPcStatus("Available");
    setModalOpen(true);
  };

  const handleEditComputerClick = (pc: Computer) => {
    setModalMode("edit");
    setEditingItem(pc);
    setPcId(pc.id);
    setPcName(pc.pcName);
    setPcZoneId(pc.zoneId);
    setPcStatus(pc.status);
    setModalOpen(true);
  };

  const handleDeleteComputerClick = (pc: Computer) => {
    const confirmed = window.confirm(`Bạn có chắc muốn xóa Máy trạm "${pc.pcName}"?`);
    if (confirmed) {
      setComputers(prev => prev.filter(item => item.id !== pc.id));
      showToast(`Đã xóa máy ${pc.pcName} thành công!`);
    }
  };

  const handleMaintenanceAll = () => {
    const targetBranch = branches.find(b => b.id === selectedBranchId);
    const scopeName = targetBranch ? `Chi nhánh "${targetBranch.branchName}"` : "toàn bộ Cyber";

    if (filteredComputers.length === 0) {
      alert(`Không có máy trạm nào thuộc ${scopeName} để bảo trì!`);
      return;
    }

    const confirmed = window.confirm(`Xác nhận chuyển trạng thái bảo trì cho tất cả máy trạm thuộc ${scopeName}?`);
    if (confirmed) {
      const filteredIds = filteredComputers.map(pc => pc.id);
      setComputers(prev =>
        prev.map(pc =>
          filteredIds.includes(pc.id)
            ? { ...pc, status: "Maintenance" as const }
            : pc
        )
      );
      showToast(`Đã bảo trì các máy thuộc ${scopeName}!`);
    }
  };

  const handleToggleSession = (pc: Computer) => {
    setComputers(prev =>
      prev.map(item => {
        if (item.id === pc.id) {
          const nextStatus = item.status === "Available" ? "Playing" : "Available";
          showToast(`Thiết lập máy ${item.pcName} thành [${nextStatus}]!`);
          return {
            ...item,
            status: nextStatus,
          };
        }
        return item;
      })
    );
  };

  const computerExtraActions: RowAction<Computer>[] = [
    {
      label: (item: Computer) => (item.status === "Available" ? "⚡ Mở máy" : "🔌 Tắt máy"),
      onClick: handleToggleSession,
      variant: "cyan",
      hidden: (item: Computer) => item.status === "Maintenance",
    },
  ];

  // ==================== 4. XỬ LÝ STATE CHO DỊCH VỤ ĐỒ ĂN (FOODS) ====================
  const handleAddFoodClick = () => {
    setModalMode("add");
    setFoodId("");
    setFoodName("");
    setFoodPrice(15000);
    setFoodCategory("Food");
    setModalOpen(true);
  };

  const handleExportFoods = () => {
    console.log("=== DANH SÁCH DỊCH VỤ ĐỒ ĂN (FOODS) ===");
    console.log(JSON.stringify(foods, null, 2));
    showToast("Đã xuất danh sách Món ăn ra Console log!");
  };

  const handleEditFoodClick = (food: Food) => {
    setModalMode("edit");
    setEditingItem(food);
    setFoodId(food.id);
    setFoodName(food.foodName);
    setFoodPrice(food.price);
    setFoodCategory(food.category);
    setModalOpen(true);
  };

  const handleDeleteFoodClick = (food: Food) => {
    const confirmed = window.confirm(`Bạn có chắc muốn xóa món ăn "${food.foodName}" khỏi thực đơn?`);
    if (confirmed) {
      setFoods(prev => prev.filter(f => f.id !== food.id));
      showToast(`Đã xóa món ${food.foodName} khỏi thực đơn!`);
    }
  };

  // ==================== LƯU THÔNG TIN TỪ MODAL FORM ====================
  const handleSave = (e: React.FormEvent) => {
    e.preventDefault();

    if (activeTab === "branches") {
      if (!branchId.trim() || !branchName.trim()) {
        alert("Vui lòng điền đầy đủ Mã và Tên chi nhánh!");
        return;
      }

      if (modalMode === "add") {
        if (branches.some(b => b.id.toLowerCase() === branchId.toLowerCase())) {
          alert("Mã Chi nhánh này đã tồn tại!");
          return;
        }
        const newBranch: Branch = {
          id: branchId.trim(),
          branchName: branchName.trim(),
          address: branchAddress.trim(),
        };
        setBranches(prev => [...prev, newBranch]);
        showToast(`Đã thêm Chi nhánh "${newBranch.branchName}" thành công!`);
      } else {
        setBranches(prev =>
          prev.map(b =>
            b.id === editingItem.id
              ? { ...b, branchName: branchName.trim(), address: branchAddress.trim() }
              : b
          )
        );
        showToast(`Đã cập nhật Chi nhánh "${branchName}"!`);
      }
    } else if (activeTab === "zones") {
      if (!zoneId.trim() || !zoneName.trim() || !zoneBranchId.trim()) {
        alert("Vui lòng điền đầy đủ thông tin khu vực!");
        return;
      }

      if (modalMode === "add") {
        if (zones.some(z => z.id.toLowerCase() === zoneId.toLowerCase())) {
          alert("Mã Khu vực này đã tồn tại!");
          return;
        }
        const newZone: Zone = {
          id: zoneId.trim(),
          zoneName: zoneName.trim(),
          pricePerHour: Number(zonePrice),
          branchId: zoneBranchId,
        };
        setZones(prev => [...prev, newZone]);
        showToast(`Đã thêm Khu vực "${newZone.zoneName}" thành công!`);
      } else {
        setZones(prev =>
          prev.map(z =>
            z.id === editingItem.id
              ? {
                  ...z,
                  zoneName: zoneName.trim(),
                  pricePerHour: Number(zonePrice),
                  branchId: zoneBranchId,
                }
              : z
          )
        );
        showToast(`Đã cập nhật Khu vực "${zoneName}"!`);
      }
    } else if (activeTab === "computers") {
      if (!pcId.trim() || !pcName.trim() || !pcZoneId.trim()) {
        alert("Vui lòng điền đầy đủ thông tin máy!");
        return;
      }

      if (modalMode === "add") {
        if (computers.some(c => c.id.toLowerCase() === pcId.toLowerCase())) {
          alert("Mã Máy trạm này đã tồn tại!");
          return;
        }
        const newPc: Computer = {
          id: pcId.trim(),
          pcName: pcName.trim(),
          zoneId: pcZoneId,
          status: pcStatus,
        };
        setComputers(prev => [...prev, newPc]);
        showToast(`Đã thêm Máy trạm "${newPc.pcName}" thành công!`);
      } else {
        setComputers(prev =>
          prev.map(c =>
            c.id === editingItem.id
              ? { ...c, pcName: pcName.trim(), zoneId: pcZoneId, status: pcStatus }
              : c
          )
        );
        showToast(`Đã cập nhật Máy trạm "${pcName}"!`);
      }
    } else {
      // activeTab === "foods"
      if (!foodId.trim() || !foodName.trim()) {
        alert("Vui lòng điền đầy đủ Mã và Tên món ăn!");
        return;
      }

      if (modalMode === "add") {
        if (foods.some(f => f.id.toLowerCase() === foodId.toLowerCase())) {
          alert("Mã Món ăn này đã tồn tại!");
          return;
        }
        const newFood: Food = {
          id: foodId.trim(),
          foodName: foodName.trim(),
          price: Number(foodPrice),
          category: foodCategory,
        };
        setFoods(prev => [...prev, newFood]);
        showToast(`Đã thêm Món "${newFood.foodName}" thành công!`);
      } else {
        setFoods(prev =>
          prev.map(f =>
            f.id === editingItem.id
              ? { ...f, foodName: foodName.trim(), price: Number(foodPrice), category: foodCategory }
              : f
          )
        );
        showToast(`Đã cập nhật Món "${foodName}"!`);
      }
    }

    setModalOpen(false);
  };

  // ==================== CÁC PHẦN RENDER PHỤ TRỢ (STASTISTICS) ====================
  const totalBranchesCount = branches.length;
  const totalZonesCount = zones.length;
  const totalPcsCount = computers.length;
  const playingPcsCount = computers.filter(c => c.status === "Playing").length;

  // Tính toán thống kê chi tiết theo yêu cầu
  // 1. Mỗi Cyber (Chi nhánh) có bao nhiêu máy trạm & bao nhiêu zone máy
  const cyberStatsList = branches.map(branch => {
    const zonesOfBranch = zones.filter(z => z.branchId === branch.id);
    const zoneIds = zonesOfBranch.map(z => z.id);
    const pcsOfBranch = computers.filter(pc => zoneIds.includes(pc.zoneId));
    return {
      branchId: branch.id,
      branchName: branch.branchName,
      zonesCount: zonesOfBranch.length,
      pcsCount: pcsOfBranch.length,
    };
  });

  // 2. Mỗi Zone máy ở mỗi Cyber có bao nhiêu máy trạm
  const zoneStatsList = zones.map(zone => {
    const pcsOfZone = computers.filter(pc => pc.zoneId === zone.id);
    const branch = branches.find(b => b.id === zone.branchId);
    return {
      zoneId: zone.id,
      zoneName: zone.zoneName,
      branchName: branch ? branch.branchName : "Không xác định",
      pcsCount: pcsOfZone.length,
    };
  });

  return (
    <div className="dashboard-container">
      {/* 1. Header */}
      <header className="cyber-header">
        <h1 className="cyber-title">Premiere Cyber Manager</h1>
      </header>

      {/* 2. Grid Thống kê nhanh */}
      <div className="stats-grid">
        <div className="stat-card">
          <span className="stat-label">Tổng Chi Nhánh (Branches)</span>
          <span className="stat-value">{totalBranchesCount}</span>
        </div>
        <div className="stat-card">
          <span className="stat-label">Tổng Khu Vực (Zones)</span>
          <span className="stat-value">{totalZonesCount}</span>
        </div>
        <div className="stat-card">
          <span className="stat-label">Tổng Máy Trạm</span>
          <span className="stat-value">{totalPcsCount}</span>
        </div>
        <div className="stat-card">
          <span className="stat-label">Đang Hoạt Động (Playing)</span>
          <span className="stat-value pink-text">{playingPcsCount}</span>
        </div>
      </div>

      {/* 3. Tabs Navigation */}
      <nav className="tabs-navigation" style={{ maxWidth: "800px" }}>
        <Button
          className={`tab-btn ${activeTab === "branches" ? "active" : ""}`}
          onClick={() => setActiveTab("branches")}
        >
          Chi Nhánh (Branches)
        </Button>
        <Button
          className={`tab-btn ${activeTab === "zones" ? "active" : ""}`}
          onClick={() => setActiveTab("zones")}
        >
          Khu Vực Máy (Zones)
        </Button>
        <Button
          className={`tab-btn ${activeTab === "computers" ? "active" : ""}`}
          onClick={() => setActiveTab("computers")}
        >
          Máy Trạm (Computers)
        </Button>
        <Button
          className={`tab-btn ${activeTab === "foods" ? "active" : ""}`}
          onClick={() => setActiveTab("foods")}
        >
          Đồ Ăn & Dịch Vụ (Foods)
        </Button>
      </nav>

      {/* 4. Khu vực Nội dung chính */}
      <main className="cyber-card">
        {activeTab === "branches" ? (
          <div>
            <div className="section-header">
              <h2 className="section-title">Quản Lý Chi Nhánh / Phòng Máy</h2>
              <CrudActionBar
                onAdd={handleAddBranchClick}
                onExport={handleExportBranches}
              />
            </div>
            
            <CrudTable<Branch>
              data={branches}
              onEdit={handleEditBranchClick}
              onDelete={handleDeleteBranchClick}
            />
          </div>
        ) : activeTab === "zones" ? (
          <div>
            <div className="section-header">
              <h2 className="section-title">Quản Lý Khu Vực Máy</h2>
              <CrudActionBar
                onAdd={handleAddZoneClick}
                onExport={handleExportZones}
              />
            </div>
            
            <CrudTable<Zone>
              data={zones}
              onEdit={handleEditZoneClick}
              onDelete={handleDeleteZoneClick}
            />
          </div>
        ) : activeTab === "computers" ? (
          <div>
            {/* Bộ lọc Máy Trạm theo từng Cyber / Chi nhánh */}
            <div className="filter-bar" style={{ marginBottom: "1.5rem", display: "flex", alignItems: "center", gap: "1rem" }}>
              <span className="form-label" style={{ margin: 0, fontWeight: 700, letterSpacing: "0.5px" }}>Lọc theo Cyber:</span>
              <select
                className="form-select"
                style={{ width: "auto", minWidth: "260px", padding: "0.5rem 2.5rem 0.5rem 1rem" }}
                value={selectedBranchId}
                onChange={(e) => setSelectedBranchId(e.target.value)}
              >
                <option value="all">-- Tất cả Cyber / Chi nhánh --</option>
                {branches.map(b => (
                  <option key={b.id} value={b.id}>{b.branchName}</option>
                ))}
              </select>
            </div>

            <div className="section-header">
              <h2 className="section-title">
                Quản Lý Máy Trạm {selectedBranchId !== "all" && `[${branches.find(b => b.id === selectedBranchId)?.branchName}]`}
              </h2>
              <CrudActionBar
                onAdd={handleAddComputerClick}
                extraActions={[
                  {
                    label: "🛠️ Bảo Trì Toàn Bộ Máy",
                    onClick: handleMaintenanceAll,
                    variant: "danger",
                  },
                ]}
              />
            </div>

            <CrudTable<Computer>
              data={filteredComputers}
              onEdit={handleEditComputerClick}
              onDelete={handleDeleteComputerClick}
              extraActions={computerExtraActions}
            />
          </div>
        ) : (
          <div>
            <div className="section-header">
              <h2 className="section-title">Quản Lý Dịch Vụ Món Ăn</h2>
              <CrudActionBar
                onAdd={handleAddFoodClick}
                onExport={handleExportFoods}
              />
            </div>

            <CrudTable<Food>
              data={foods}
              onEdit={handleEditFoodClick}
              onDelete={handleDeleteFoodClick}
            />
          </div>
        )}
      </main>

      {/* 4.5. Báo cáo thống kê chi tiết (Cyber Analytics) */}
      <section className="analytics-section">
        <div className="analytics-grid">
          {/* Card 1: Thống kê chi tiết theo Chi nhánh */}
          <div className="analytics-card">
            <h3 className="analytics-title">Thống Kê Theo Chi Nhánh (Cyber)</h3>
            <ul className="analytics-list">
              {cyberStatsList.length === 0 ? (
                <li className="analytics-item text-center empty-row" style={{ listStyle: "none" }}>
                  Chưa có chi nhánh nào để thống kê.
                </li>
              ) : (
                cyberStatsList.map(stat => (
                  <li key={stat.branchId} className="analytics-item">
                    <div className="analytics-info">
                      <span className="analytics-name">{stat.branchName}</span>
                      <span className="analytics-subname">Mã Cyber: {stat.branchId}</span>
                    </div>
                    <div className="analytics-badges">
                      <span className="analytics-badge-item badge-purple">
                        {stat.zonesCount} Zones
                      </span>
                      <span className="analytics-badge-item badge-cyan">
                        {stat.pcsCount} Máy trạm
                      </span>
                    </div>
                  </li>
                ))
              )}
            </ul>
          </div>

          {/* Card 2: Thống kê chi tiết theo Zone máy của từng Cyber */}
          <div className="analytics-card">
            <h3 className="analytics-title">Thống Kê Theo Khu Vực Máy (Zones)</h3>
            <ul className="analytics-list">
              {zoneStatsList.length === 0 ? (
                <li className="analytics-item text-center empty-row" style={{ listStyle: "none" }}>
                  Chưa có khu vực máy nào để thống kê.
                </li>
              ) : (
                zoneStatsList.map(stat => (
                  <li key={stat.zoneId} className="analytics-item">
                    <div className="analytics-info">
                      <span className="analytics-name">{stat.zoneName}</span>
                      <span className="analytics-subname">Thuộc Cyber: {stat.branchName}</span>
                    </div>
                    <div className="analytics-badges">
                      <span className="analytics-badge-item badge-cyan">
                        {stat.pcsCount} Máy trạm
                      </span>
                    </div>
                  </li>
                ))
              )}
            </ul>
          </div>
        </div>
      </section>

      {/* 5. Custom Premium Modal Form */}
      {modalOpen && (
        <div className="modal-overlay">
          <div className="modal-content">
            <div className="modal-header">
              <h3 className="modal-title">
                {modalMode === "add" ? "Thêm Mới" : "Cập Nhật"}{" "}
                {activeTab === "branches"
                  ? "Chi Nhánh"
                  : activeTab === "zones"
                  ? "Khu Vực Máy"
                  : activeTab === "computers"
                  ? "Máy Trạm"
                  : "Món Ăn"}
              </h3>
            </div>

            <form onSubmit={handleSave}>
              <div className="modal-body">
                {activeTab === "branches" ? (
                  // Form Fields cho Branch
                  <>
                    <div className="form-group">
                      <label className="form-label">Mã Chi Nhánh (ID)</label>
                      <input
                        type="text"
                        className="form-input"
                        placeholder="Ví dụ: B-Q1, B-Q10"
                        value={branchId}
                        onChange={(e) => setBranchId(e.target.value)}
                        disabled={modalMode === "edit"}
                        required
                      />
                    </div>
                    <div className="form-group">
                      <label className="form-label">Tên Chi Nhánh</label>
                      <input
                        type="text"
                        className="form-input"
                        placeholder="Ví dụ: Cyber Q1 - Nguyễn Du"
                        value={branchName}
                        onChange={(e) => setBranchName(e.target.value)}
                        required
                      />
                    </div>
                    <div className="form-group">
                      <label className="form-label">Địa Chỉ Chi Nhánh</label>
                      <input
                        type="text"
                        className="form-input"
                        placeholder="Ví dụ: 79 Nguyễn Du, Quận 1, TP.HCM"
                        value={branchAddress}
                        onChange={(e) => setBranchAddress(e.target.value)}
                        required
                      />
                    </div>
                  </>
                ) : activeTab === "zones" ? (
                  // Form Fields cho Zone
                  <>
                    <div className="form-group">
                      <label className="form-label">Mã Khu Vực (ID)</label>
                      <input
                        type="text"
                        className="form-input"
                        placeholder="Ví dụ: Z-VIP-Q1"
                        value={zoneId}
                        onChange={(e) => setZoneId(e.target.value)}
                        disabled={modalMode === "edit"}
                        required
                      />
                    </div>
                    <div className="form-group">
                      <label className="form-label">Tên Khu Vực</label>
                      <input
                        type="text"
                        className="form-input"
                        placeholder="Ví dụ: Khu Premium VIP"
                        value={zoneName}
                        onChange={(e) => setZoneName(e.target.value)}
                        required
                      />
                    </div>
                    <div className="form-group">
                      <label className="form-label">Giá Theo Giờ (VND/h)</label>
                      <input
                        type="number"
                        className="form-input"
                        placeholder="Ví dụ: 15000"
                        min="0"
                        step="500"
                        value={zonePrice}
                        onChange={(e) => setZonePrice(Number(e.target.value))}
                        required
                      />
                    </div>
                    <div className="form-group">
                      <label className="form-label">Thuộc Chi Nhánh (Branch)</label>
                      <select
                        className="form-select"
                        value={zoneBranchId}
                        onChange={(e) => setZoneBranchId(e.target.value)}
                        required
                      >
                        {branches.map((branch) => (
                          <option key={branch.id} value={branch.id}>
                            {branch.branchName} ({branch.id})
                          </option>
                        ))}
                      </select>
                    </div>
                  </>
                ) : activeTab === "computers" ? (
                  // Form Fields cho Computer
                  <>
                    <div className="form-group">
                      <label className="form-label">Mã Máy (ID)</label>
                      <input
                        type="text"
                        className="form-input"
                        placeholder="Ví dụ: PC-05"
                        value={pcId}
                        onChange={(e) => setPcId(e.target.value)}
                        disabled={modalMode === "edit"}
                        required
                      />
                    </div>
                    <div className="form-group">
                      <label className="form-label">Tên Máy Trạm</label>
                      <input
                        type="text"
                        className="form-input"
                        placeholder="Ví dụ: Máy Số 05"
                        value={pcName}
                        onChange={(e) => setPcName(e.target.value)}
                        required
                      />
                    </div>
                    <div className="form-group">
                      <label className="form-label">Thuộc Khu Vực (Zone)</label>
                      <select
                        className="form-select"
                        value={pcZoneId}
                        onChange={(e) => setPcZoneId(e.target.value)}
                        required
                      >
                        {/* Lọc Zone trong dropdown: nếu đang chọn một Cyber cụ thể thì chỉ hiện Zone của Cyber đó */}
                        {(selectedBranchId === "all"
                          ? zones
                          : zones.filter(z => z.branchId === selectedBranchId)
                        ).map((zone) => {
                          const branch = branches.find(b => b.id === zone.branchId);
                          const branchLabel = branch ? ` - ${branch.branchName}` : "";
                          return (
                            <option key={zone.id} value={zone.id}>
                              {zone.zoneName}{branchLabel} ({new Intl.NumberFormat("vi-VN").format(zone.pricePerHour)}đ/h)
                            </option>
                          );
                        })}
                      </select>
                    </div>
                    <div className="form-group">
                      <label className="form-label">Trạng Thái Ban Đầu</label>
                      <select
                        className="form-select"
                        value={pcStatus}
                        onChange={(e) =>
                          setPcStatus(e.target.value as "Available" | "Playing" | "Maintenance")
                        }
                      >
                        <option value="Available">Available</option>
                        <option value="Playing">Playing</option>
                        <option value="Maintenance">Maintenance</option>
                      </select>
                    </div>
                  </>
                ) : (
                  // Form Fields cho Foods
                  <>
                    <div className="form-group">
                      <label className="form-label">Mã Món Ăn (ID)</label>
                      <input
                        type="text"
                        className="form-input"
                        placeholder="Ví dụ: F-10"
                        value={foodId}
                        onChange={(e) => setFoodId(e.target.value)}
                        disabled={modalMode === "edit"}
                        required
                      />
                    </div>
                    <div className="form-group">
                      <label className="form-label">Tên Món Ăn</label>
                      <input
                        type="text"
                        className="form-input"
                        placeholder="Ví dụ: Trà Đào Cam Sả"
                        value={foodName}
                        onChange={(e) => setFoodName(e.target.value)}
                        required
                      />
                    </div>
                    <div className="form-group">
                      <label className="form-label">Đơn Giá (VND)</label>
                      <input
                        type="number"
                        className="form-input"
                        placeholder="Ví dụ: 25000"
                        min="0"
                        step="1000"
                        value={foodPrice}
                        onChange={(e) => setFoodPrice(Number(e.target.value))}
                        required
                      />
                    </div>
                    <div className="form-group">
                      <label className="form-label">Phân Loại (Category)</label>
                      <select
                        className="form-select"
                        value={foodCategory}
                        onChange={(e) =>
                          setFoodCategory(e.target.value as "Drink" | "Food" | "Combo")
                        }
                      >
                        <option value="Food">Đồ Ăn (Food)</option>
                        <option value="Drink">Thức Uống (Drink)</option>
                        <option value="Combo">Combo Tiết Kiệm (Combo)</option>
                      </select>
                    </div>
                  </>
                )}
              </div>

              <div className="modal-footer">
                <Button
                  variant="secondary"
                  onClick={() => setModalOpen(false)}
                >
                  Hủy bỏ
                </Button>
                <Button
                  type="submit"
                  variant="primary"
                >
                  Xác nhận
                </Button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* 6. Toast Notification */}
      {toastMessage && (
        <div className="toast-msg">
          <span>📡</span>
          <span>{toastMessage}</span>
        </div>
      )}
    </div>
  );
};

export default GenericDashboard;

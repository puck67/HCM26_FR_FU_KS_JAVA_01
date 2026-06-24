import React, { useRef } from 'react';
import { modelRegistry } from '../config/modelRegistry';
import { useCrud } from '../hooks/useCrud';
import { CrudTable } from '../components/CrudTable';
import { CrudActionBar } from '../components/CrudActionBar';
import { CrudModal } from '../components/CrudModal';
import { Toast } from '../components/Toast';
import { CheckIcon, LockIcon, UnlockIcon, SearchIcon } from '../components/Icons';

export function Dashboard() {
  const {
    activeTab,
    selectTab,
    searchQuery,
    setSearchQuery,
    toasts,
    removeToast,
    showToast,
    isModalOpen,
    setIsModalOpen,
    editingItem,
    openAddModal,
    openEditModal,
    deleteItem,
    saveItem,
    patchItem,
    importData,
    exportData,
    getActiveData,
    getFilteredData
  } = useCrud(modelRegistry, 'tasks');

  // File Upload Ref for Importing JSON
  const fileInputRef = useRef(null);

  // Active configurations based on current model key
  const activeModelConfig = modelRegistry[activeTab];

  // Specific extra buttons based on selected model
  const getExtraActions = (item) => {
    if (activeTab === 'tasks') {
      if (item.status === 'Pending') {
        return [
          {
            title: "Complete",
            action: () => {
              patchItem(item.id, { status: 'Completed' });
              showToast('success', `Task completed!`, item.title);
            },
            variant: "success",
            icon: <CheckIcon />
          }
        ];
      }
    } else {
      const isPersonActive = item.status === 'Active';
      return [
        {
          title: isPersonActive ? "Deactivate" : "Activate",
          action: () => {
            const newStatus = isPersonActive ? 'Inactive' : 'Active';
            patchItem(item.id, { status: newStatus });
            showToast('info', `Status changed to ${newStatus}`, item.name);
          },
          variant: isPersonActive ? "danger" : "success",
          icon: isPersonActive ? <LockIcon /> : <UnlockIcon />
        }
      ];
    }
    return [];
  };

  // Dynamically calculate KPIs based on selected model configurations
  const getKPIs = () => {
    const data = getActiveData();
    const total = data.length;

    if (activeTab === 'tasks') {
      const completed = data.filter(t => t.status === 'Completed').length;
      const pending = total - completed;
      return [
        { title: "Total Tasks", value: total, type: "info", icon: activeModelConfig.icon },
        { title: "Completed Tasks", value: completed, type: "success", icon: <CheckIcon /> },
        { title: "Pending Tasks", value: pending, type: "warning", icon: <UnlockIcon /> }
      ];
    } else {
      const activeName = activeTab.charAt(0).toUpperCase() + activeTab.slice(1);
      const active = data.filter(s => s.status === 'Active').length;
      const inactive = total - active;
      return [
        { title: `Total ${activeName}`, value: total, type: "info", icon: activeModelConfig.icon },
        { title: `Active ${activeName}`, value: active, type: "success", icon: <UnlockIcon /> },
        { title: `Inactive ${activeName}`, value: inactive, type: "danger", icon: <LockIcon /> }
      ];
    }
  };

  const handleImportClick = () => {
    fileInputRef.current.click();
  };

  const handleFileImport = (e) => {
    const file = e.target.files[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = (event) => {
      try {
        const parsed = JSON.parse(event.target.result);
        if (Array.isArray(parsed)) {
          importData(parsed);
        } else {
          showToast('danger', 'Import failed', 'JSON file must contain an array of items.');
        }
      } catch (err) {
        showToast('danger', 'Import failed', `Failed to parse file: ${err.message}`);
      }
    };
    reader.readAsText(file);
    e.target.value = '';
  };

  return (
    <div className="dashboard-layout">
      {/* Sidebar Navigation */}
      <aside className="sidebar">
        <div>
          <div className="brand-section">
            <div className="brand-logo">{modelRegistry.students.icon}</div>
            <h2 className="brand-name">Nexus Admin</h2>
          </div>
          
          <ul className="nav-links">
            {Object.values(modelRegistry).map((model) => (
              <li className="nav-item" key={model.key}>
                <button
                  className={`nav-button ${activeTab === model.key ? 'active' : ''}`}
                  onClick={() => selectTab(model.key)}
                >
                  {model.icon} {model.label}
                </button>
              </li>
            ))}
          </ul>
        </div>
      </aside>

      {/* Main Container */}
      <main className="main-content">
        <header className="header-container">
          <div className="header-title">
            <h1>{activeModelConfig.label}</h1>
            <p>Manage list records, perform CRUD operations, and export/import data datasets.</p>
          </div>
        </header>

        {/* Dynamic KPI Cards */}
        <section className="kpi-grid">
          {getKPIs().map((kpi, idx) => (
            <div key={idx} className={`kpi-card kpi-${kpi.type}`}>
              <span className="kpi-title">{kpi.title}</span>
              <div className="kpi-value-container">
                <span className="kpi-value">{kpi.value}</span>
                <span className="kpi-icon">{kpi.icon}</span>
              </div>
            </div>
          ))}
        </section>

        {/* Control Bar: Search and CRUD Action Bar */}
        <section className="control-bar">
          <div className="search-container">
            <span className="search-icon"><SearchIcon /></span>
            <input 
              type="text" 
              placeholder={`Search ${activeTab}...`} 
              className="search-input"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
            />
          </div>
          
          <CrudActionBar 
            onAdd={openAddModal}
            onImport={handleImportClick}
            onExport={exportData}
          />
          
          {/* Hidden Import File Input */}
          <input 
            type="file" 
            ref={fileInputRef} 
            style={{ display: 'none' }} 
            accept=".json"
            onChange={handleFileImport}
          />
        </section>

        {/* Generic CRUD Table */}
        <section style={{ flexGrow: 1 }}>
          <CrudTable 
            data={getFilteredData()}
            columns={activeModelConfig.columns}
            onEdit={openEditModal}
            onDelete={deleteItem}
            extraActions={getExtraActions}
          />
        </section>
      </main>

      {/* Generic Modal Form */}
      <CrudModal 
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onSubmit={saveItem}
        title={editingItem ? `Edit Record` : `Add New Record`}
        initialData={editingItem}
        fields={activeModelConfig.fields}
      />

      {/* Global Toast Alert Notifications */}
      <Toast 
        toasts={toasts}
        removeToast={removeToast}
      />
    </div>
  );
}
export default Dashboard;

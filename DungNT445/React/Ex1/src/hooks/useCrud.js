import { useState } from 'react';

export function useCrud(registry, defaultTab) {
  const [activeTab, setActiveTab] = useState(defaultTab);
  const [searchQuery, setSearchQuery] = useState('');
  
  // Dynamically initialize state store for each registry key
  const [dataStore, setDataStore] = useState(() => {
    const initial = {};
    Object.keys(registry).forEach(key => {
      initial[key] = registry[key].initialData || [];
    });
    return initial;
  });

  const [toasts, setToasts] = useState([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingItem, setEditingItem] = useState(null);

  // Get data for the active tab
  const getActiveData = () => dataStore[activeTab] || [];

  // Filter data based on search query
  const getFilteredData = () => {
    const data = getActiveData();
    if (!searchQuery) return data;
    const lowerQuery = searchQuery.toLowerCase();
    return data.filter(item =>
      Object.values(item).some(val =>
        String(val).toLowerCase().includes(lowerQuery)
      )
    );
  };

  // Toast Helpers
  const showToast = (type, message, subtext = '') => {
    const id = Date.now();
    setToasts(prev => [...prev, { id, type, message, subtext }]);
    setTimeout(() => {
      setToasts(prev => prev.filter(t => t.id !== id));
    }, 4000);
  };

  const removeToast = (id) => {
    setToasts(prev => prev.filter(t => t.id !== id));
  };

  // Actions
  const openAddModal = () => {
    setEditingItem(null);
    setIsModalOpen(true);
  };

  const openEditModal = (item) => {
    setEditingItem(item);
    setIsModalOpen(true);
  };

  const deleteItem = (item) => {
    const displayName = item.name || item.title || `ID: ${item.id}`;
    if (window.confirm(`Are you sure you want to delete "${displayName}"?`)) {
      setDataStore(prev => ({
        ...prev,
        [activeTab]: prev[activeTab].filter(x => x.id !== item.id)
      }));
      showToast('danger', `Deleted record`, displayName);
    }
  };

  const saveItem = (formData) => {
    const displayName = formData.name || formData.title || `ID: ${formData.id}`;
    if (formData.id) {
      // Edit
      setDataStore(prev => ({
        ...prev,
        [activeTab]: prev[activeTab].map(x => x.id === formData.id ? formData : x)
      }));
      showToast('success', `Updated successfully!`, displayName);
    } else {
      // Create
      const newRecord = {
        ...formData,
        id: Date.now()
      };
      setDataStore(prev => ({
        ...prev,
        [activeTab]: [...prev[activeTab], newRecord]
      }));
      showToast('success', `Created new record!`, displayName);
    }
    setIsModalOpen(false);
  };

  // Modify specific property of a record (e.g. toggle status)
  const patchItem = (id, updates) => {
    setDataStore(prev => ({
      ...prev,
      [activeTab]: prev[activeTab].map(x => x.id === id ? { ...x, ...updates } : x)
    }));
  };

  // Import JSON list into active store
  const importData = (importedList) => {
    const validated = importedList.map((item, index) => ({
      ...item,
      id: Date.now() + index
    }));
    setDataStore(prev => ({
      ...prev,
      [activeTab]: [...prev[activeTab], ...validated]
    }));
    showToast('success', `Imported ${validated.length} items!`);
  };

  // Export JSON file
  const exportData = () => {
    const data = getActiveData();
    const dataStr = "data:text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(data, null, 2));
    const downloadAnchor = document.createElement('a');
    downloadAnchor.setAttribute("href", dataStr);
    downloadAnchor.setAttribute("download", `${activeTab}_data_export.json`);
    document.body.appendChild(downloadAnchor);
    downloadAnchor.click();
    downloadAnchor.remove();
    showToast('success', `Export successful!`, `${activeTab}_data_export.json downloaded`);
  };

  const selectTab = (tabKey) => {
    setActiveTab(tabKey);
    setSearchQuery('');
  };

  return {
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
    getFilteredData,
    dataStore
  };
}

import { useState } from 'react'
import Dashboard from './pages/Dashboard'
import SubjectNotesPage from './pages/SubjectNotesPage'
import NotesListPage from './pages/NotesListPage'
import NoteEditorPage from './pages/NoteEditorPage'
import Sidebar from './components/Sidebar'
import './index.css'

export default function App() {
  const [route, setRoute] = useState({ page: 'dashboard', params: {} })

  const navigate = (page, params = {}) => setRoute({ page, params })

  const renderPage = () => {
    switch (route.page) {
      case 'dashboard':    return <Dashboard navigate={navigate} />
      case 'subject-notes': return <SubjectNotesPage navigate={navigate} />
      case 'notes-list':   return <NotesListPage navigate={navigate} subjectNoteId={route.params.subjectNoteId} subjectNoteTitle={route.params.title} />
      case 'note-editor':  return <NoteEditorPage navigate={navigate} noteId={route.params.noteId} noteTitle={route.params.noteTitle} />
      default:             return <Dashboard navigate={navigate} />
    }
  }

  // Note editor is full-screen, no sidebar
  if (route.page === 'note-editor') {
    return (
      <div style={{ display: 'flex', height: '100vh' }}>
        <Sidebar navigate={navigate} currentPage={route.page} />
        {renderPage()}
      </div>
    )
  }

  return (
    <div className="app-shell">
      <Sidebar navigate={navigate} currentPage={route.page} />
      <main className="app-main">
        {renderPage()}
      </main>
    </div>
  )
}

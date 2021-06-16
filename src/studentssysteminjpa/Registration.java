/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentssysteminjpa;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Mohammed
 */
@Entity
@Table(name = "registration")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Registration.findAll", query = "SELECT r FROM Registration r")
    , @NamedQuery(name = "Registration.findByStudentId", query = "SELECT r FROM Registration r WHERE r.registrationPK.studentId = :studentId")
    , @NamedQuery(name = "Registration.findByCourseId", query = "SELECT r FROM Registration r WHERE r.registrationPK.courseId = :courseId")
    , @NamedQuery(name = "Registration.findBySemester", query = "SELECT r FROM Registration r WHERE r.semester = :semester")})
public class Registration implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RegistrationPK registrationPK;
    @Basic(optional = false)
    @Column(name = "semester")
    private int semester;
    @JoinColumn(name = "courseId", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Course course;
    @JoinColumn(name = "studentId", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Student student;

    public Registration() {
    }

    public Registration(RegistrationPK registrationPK) {
        this.registrationPK = registrationPK;
    }

    public Registration(RegistrationPK registrationPK, int semester) {
        this.registrationPK = registrationPK;
        this.semester = semester;
    }

    public Registration(int studentId, String courseId) {
        this.registrationPK = new RegistrationPK(studentId, courseId);
    }

    public RegistrationPK getRegistrationPK() {
        return registrationPK;
    }

    public void setRegistrationPK(RegistrationPK registrationPK) {
        this.registrationPK = registrationPK;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (registrationPK != null ? registrationPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Registration)) {
            return false;
        }
        Registration other = (Registration) object;
        if ((this.registrationPK == null && other.registrationPK != null) || (this.registrationPK != null && !this.registrationPK.equals(other.registrationPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "studentssysteminjpa.Registration[ registrationPK=" + registrationPK + " ]";
    }
    
}

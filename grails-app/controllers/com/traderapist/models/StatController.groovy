package com.traderapist.models

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.access.annotation.Secured

@Secured(['ROLE_ADMIN'])
class StatController {

	def sessionFactory
	def propertyInstanceMap = org.codehaus.groovy.grails.plugins.DomainClassGrailsPlugin.PROPERTY_INSTANCE_MAP

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [statInstanceList: Stat.list(params), statInstanceTotal: Stat.count()]
    }

    def create() {
        [statInstance: new Stat(params)]
    }

    def save() {
        def statInstance = new Stat(params)
        if (!statInstance.save(flush: true)) {
            render(view: "create", model: [statInstance: statInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'stat.label', default: 'Stat'), statInstance.id])
        redirect(action: "show", id: statInstance.id)
    }

    def show(Long id) {
        def statInstance = Stat.get(id)
        if (!statInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'stat.label', default: 'Stat'), id])
            redirect(action: "list")
            return
        }

        [statInstance: statInstance]
    }

    def edit(Long id) {
        def statInstance = Stat.get(id)
        if (!statInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'stat.label', default: 'Stat'), id])
            redirect(action: "list")
            return
        }

        [statInstance: statInstance]
    }

    def update(Long id, Long version) {
        def statInstance = Stat.get(id)
        if (!statInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'stat.label', default: 'Stat'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (statInstance.version > version) {
                statInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'stat.label', default: 'Stat')] as Object[],
                          "Another user has updated this Stat while you were editing")
                render(view: "edit", model: [statInstance: statInstance])
                return
            }
        }

        statInstance.properties = params

        if (!statInstance.save(flush: true)) {
            render(view: "edit", model: [statInstance: statInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'stat.label', default: 'Stat'), statInstance.id])
        redirect(action: "show", id: statInstance.id)
    }

    def delete(Long id) {
        def statInstance = Stat.get(id)
        if (!statInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'stat.label', default: 'Stat'), id])
            redirect(action: "list")
            return
        }

        try {
            statInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'stat.label', default: 'Stat'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'stat.label', default: 'Stat'), id])
            redirect(action: "show", id: id)
        }
    }

	def dumpToCSV() {
		def players = Player.list()
		def csv = ""

		players.eachWithIndex { player, i ->
			csv += Stat.dumpToCSV(player)

			if(i%100 == 0) {
				sessionFactory.currentSession.flush()
				sessionFactory.currentSession.clear()
				propertyInstanceMap.get().clear()
			}

			println "Finished writing stats for ${player.name} (${i}/${players.size()})."
		}

		File f = new File("./dump.csv")
		f.write(csv)

		render "done"
	}
}
